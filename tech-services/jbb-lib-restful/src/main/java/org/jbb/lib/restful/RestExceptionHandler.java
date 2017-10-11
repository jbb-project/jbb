/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful;

import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, WebRequest request) {
        return buildResponseEntity(ErrorInfo.INTERNAL_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
        HttpRequestMethodNotSupportedException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {

        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        if (!CollectionUtils.isEmpty(supportedMethods)) {
            headers.setAllow(supportedMethods);
        }
        return buildResponseEntity(ErrorInfo.METHOD_NOT_SUPPORTED, headers);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
        HttpMediaTypeNotSupportedException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            headers.setAccept(mediaTypes);
        }

        return buildResponseEntity(ErrorInfo.UNSUPPORTED_MEDIA_TYPE, headers);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
        HttpMediaTypeNotAcceptableException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(ErrorInfo.NOT_ACCEPTABLE_MEDIA_TYPE);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(ErrorInfo.MISSING_PATH_VARIABLE);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        MissingServletRequestParameterException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(ErrorInfo.MISSING_REQUEST_PARAMETER);
    }


    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
        ServletRequestBindingException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(ErrorInfo.REQUEST_BINDING_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(
        ConversionNotSupportedException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(ErrorInfo.CONVERSION_NOT_SUPPORTED);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
        HttpHeaders headers,
        HttpStatus status, WebRequest request) {
        return buildResponseEntity(ErrorInfo.TYPE_MISMATCH);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(ErrorInfo.MESSAGE_NOT_READABLE);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
        HttpMessageNotWritableException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(ErrorInfo.MESSAGE_NOT_WRITABLE);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(ErrorInfo.VALIDATION_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
        MissingServletRequestPartException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(ErrorInfo.MISSING_REQUEST_PART);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers,
        HttpStatus status, WebRequest request) {
        return buildResponseEntity(ErrorInfo.BIND_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
        NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(ErrorInfo.NO_HANDLER_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
        AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status,
        WebRequest webRequest) {

        if (webRequest instanceof ServletWebRequest) {
            ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest;
            HttpServletRequest request = servletWebRequest.getRequest();
            HttpServletResponse response = servletWebRequest.getResponse();
            if (response != null && response.isCommitted()) {
                if (logger.isErrorEnabled()) {
                    logger.error(
                        "Async timeout for " + request.getMethod() + " [" + request.getRequestURI()
                            + "]");
                }
                return null;
            }
        }

        return buildResponseEntity(ErrorInfo.ASYNC_REQUEST_TIMEOUT);
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorInfo errorInfo) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(errorInfo);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorInfo errorInfo, HttpHeaders headers) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(errorInfo);
        return new ResponseEntity<>(errorResponse, headers, errorResponse.getStatus());
    }

}
