package ie.gtludwig.pa.service;

import ie.gtludwig.pa.model.UserProfile;
import ie.gtludwig.pa.service.generic.CrudService;

import java.util.List;
import java.util.Set;

public interface UserProfileService extends CrudService<UserProfile> {

    UserProfile findById(String id);

	UserProfile findUserProfileByType(String type);

//	List<UserProfile> findAllUserProfilesByUsername(String username);

	List<UserProfile> findAllUserProfilesByUserId(String userId);

    Set<UserProfile> getSelfRegistrationUserProfileSet();

    Set<UserProfile> getInvitedSpecialistUserProfileSet();


}
