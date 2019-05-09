package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.UserProfile;
import ie.gtludwig.pa.service.generic.CrudService;

import java.util.List;

public interface UserProfileService extends CrudService<UserProfile> {

    UserProfile findById(String id);

	UserProfile findUserProfileByType(String type);

	List<UserProfile> findAllUserProfilesByUsername(String username);

	List<UserProfile> findAllUserProfilesByUserId(String ssoId);
}
