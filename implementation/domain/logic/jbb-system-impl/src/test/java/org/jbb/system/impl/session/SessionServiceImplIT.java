package org.jbb.system.impl.session;

import org.jbb.lib.core.CoreConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.logging.LoggingConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.lib.test.SpringSecurityConfigMocks;
import org.jbb.system.api.service.SessionService;
import org.jbb.system.impl.SystemConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringSecurityConfigMocks.class,MvcConfig.class,
        CoreConfig.class, SystemConfig.class,
        LoggingConfig.class, EventBusConfig.class,
        PropertiesConfig.class, DbConfig.class,
        CoreConfigMocks.class})
public class SessionServiceImplIT {

    @Autowired
    private SessionService sessionService;


    @Test
    public void whenSomeoneIsLogInThenShouldExistsInSessionMap(){

    }

    @Test
    public void whenTwoUsersAreLogInThenBothShouldExistsInSessionMap(){

    }

    @Test
    public void whenOneOfTwoUsersWillLogOffThenOnlyOneUserShouldExistsInSessionMap(){

    }

    @Test
    public void whenNoOneIsLogInThenMapSessionBeEmpty(){

    }

    @Test
    public void afterInactiveSessionTimeSessionWhichAreApplicableShouldBeRemoved(){

    }

    @Test
    public void whenInactiveSessionTimePropertiesIsChangeThenNewValueShouldBeSaved(){

    }
}
