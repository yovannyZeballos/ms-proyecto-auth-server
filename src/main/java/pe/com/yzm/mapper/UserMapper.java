package pe.com.yzm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.com.yzm.expose.request.UserCreateRequest;
import pe.com.yzm.model.User;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

  @Mapping(target = "roleId", constant = "1L")
  public abstract User userCreateRequestToUser(UserCreateRequest userRequest);
}
