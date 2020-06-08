Feature: Crear una cuenta de usuario

  @e2e
  Scenario Outline: Deberia crearse una cuenta de usuario
    Given El usuario esta en el formulario para crear una cuenta y llena los datos (nombre, apellido, celular, dirección)
    When El usuario digita el nombre <name>
    And El usuario digita el apellido <lastname>
    And El usuario digita el numero de celular <cellphone>
    And El usuario digita la dirección <address>
    And El usuario da click en el boton enviar
    Then El usuario se redirecciona a la pantalla donde están todos los usuarios
    Examples:
      | juan   | gomez      | 3112222 | guanajuato 45 |
      | daniel | santamaria | 111111  | valle 45      |



