package pe.com.yzm.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * <b>Class</b>: User <br/>
 * <b>Copyright</b>: 2024 Yovanny Zeballos Medina<br/>.
 *
 * @author 2024  Yovanny Zeballos Medina <br/>
 * <u>Developed by</u>: Yovanny Zeballos <br/>
 * <u>Changes:</u><br/>
 * <ul>
 *   <li>
 *     setiembre 03, 2024 Creación de Clase.
 *   </li>
 * </ul>
 */
@Table(name = "usuarios", schema = "public")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
  @Id
  Long id;

  @Column("nombre")
  String name;

  @Column("email")
  String email;

  @Column("clave")
  String password;

  @Column("rol_id")
  Long roleId;

  @Column("usuario_id")
  Long userId;
}
