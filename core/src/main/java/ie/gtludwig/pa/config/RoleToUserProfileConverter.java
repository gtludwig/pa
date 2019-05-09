package ie.gtludwig.pa.config;

import ie.gtludwig.pa.model.UserProfile;
import ie.gtludwig.pa.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class RoleToUserProfileConverter implements Converter<Object, UserProfile> {

    @Autowired
    UserProfileService userProfileService;

    public UserProfile convert(Object element) {
        return userProfileService.findUserProfileByType((String) element);
    }
}
