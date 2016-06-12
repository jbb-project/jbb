/*
 * Copyright (c) 2012-2015, Luigi R. Viggiano
 * All rights reserved.
 *
 * This software is distributable under the BSD license.
 * See the terms of the BSD license in the documentation provided with this software.
 */

package org.aeonbits.owner;

/**
 * Tokenizer implementation based on {@link String#split(String, int)} and {@link String#trim()}.
 * This class is used to implement <tt>tokenizer</tt>s for the {@link Config.Separator} annotation.
 *
 * @author Luigi R. Viggiano
 * @since 1.0.4
 */
class SplitAndTrimTokenizer implements Tokenizer {

    private final String regex;

    public SplitAndTrimTokenizer(String regex) {
        this.regex = regex;
    }

    public String[] tokens(String values) {
        String[] chunks = values.split(regex, -1);
        for (int i = 0; i < chunks.length; i++)
            chunks[i] = chunks[i].trim();
        return chunks;
    }

}
