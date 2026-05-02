# 📝 Roadmap del Proyecto Quizmael

## 🚨 Prioridad Alta (Bloqueantes)
- [ ] Implementar validaciones completas en el registro de usuario (Regex y encriptación en `AuthServiceImpl`).
- [ ] Solucionar carga perezosa en `AdminPanel2` (Evitar `NullPointerException` al cargar datos de sesión).
- [ ] Flujo completo del juego: Conectar `QuizSelectionPanel` -> `PlayPanel` -> `ResultsPanel`.

## 🟡 Prioridad Media (Funcionalidad)
- [ ] Lógica completa del panel de administración (CRUD de usuarios: eliminar, cambiar contraseña, roles).
- [ ] Internacionalización (i18n) de las vistas principales mediante `ResourceBundle`.
- [ ] Generación de informes en PDF con JasperReports (estadísticas del usuario).

## 🟢 Prioridad Baja (Mejoras Visuales / Extras)
- [ ] Aplicar estilos personalizados y temas visuales (Look and Feel) para Swing.
- [ ] Refactorizar listeners de NetBeans GUI Builder a métodos `initCustomComponents()`.

---

# 🤔 Dudas y Notas Técnicas
*   **Hibernate:** Recordar revisar las anotaciones `@OneToMany` y `@ManyToOne` si hay problemas de Lazy Initialization.
*   **H2:** Asegurar que la ruta en el empaquetado final sea correcta (`jdbc:h2:./data/quizmael`).
*   **Empaquetado:** Probar Launch4j con un JRE portable antes de la entrega final.