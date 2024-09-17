package pe.com.yzm.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConstantMessage {

  public static final String USER_EXCEPTION = "Error to authenticate user";
  public static final String USER_ICORRECT = "User or password incorrect";
  public static final String JWT_INCORRECT = "Incorrect token";
  public final String ERROR_SAVE_USER = "Error al guardar el usuario";
  public final String USER_EXIST = "Usuario %s ya existe";

}
