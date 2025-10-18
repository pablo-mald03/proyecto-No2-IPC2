import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { TipoUsuarioEnum } from '../../../models/usuarios/tipo-usuario-enum';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { UsuarioService } from '../../../services/usuarios-service/usuario.service';

@Component({
  selector: 'app-crear-admin-cine',
  imports: [FormsModule, ReactiveFormsModule,RouterLink,RouterLinkActive],
  templateUrl: './crear-admin-cine.component.html',
  styleUrl: './crear-admin-cine.component.scss'
})
export class CrearAdminCineComponent implements OnInit{



  //Atributos para crear un nuevo usuario
  nuevoRegistroForm!: FormGroup;
  tipoUsuarioEnum = TipoUsuarioEnum;

  archivoSeleccionado!: File | null;

  router = inject(Router);

  constructor(
    private formBuilder: FormBuilder,

    //Pendiente cambiar el service
    private usuarioService: UsuarioService
  ) {

  }

  ngOnInit(): void {

    this.nuevoRegistroForm = this.formBuilder.group(
      {
        nombre: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
        identificacion: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
        id: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
        password: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(5)]],
        confirmpassword: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(5)]],
        correo: [null, [Validators.required, Validators.maxLength(150), Validators.email,  Validators.pattern(/^[a-zA-Z0-9._%+-]+@cinema\.com$/)]],
        telefono: [null, [Validators.required, Validators.maxLength(150), Validators.pattern('^[0-9]+$')]],
        codigoRol: [TipoUsuarioEnum.ADMINISTRADOR_CINE, Validators.required],
        pais: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
      }
    )

  }

  submit(): void {
    if (this.nuevoRegistroForm.valid) {

      const formData = new FormData();

      Object.entries(this.nuevoRegistroForm.value).forEach(([key, value]) => {
        formData.append(key, value as string);
      });

      formData.append('codigoRol', 'ADMINISTRADOR_CINE');

      if (this.archivoSeleccionado) {
        formData.append('foto', this.archivoSeleccionado);
      }

      //Pendiente conectar al backend
      /*this.usuarioService.crearNuevoUsuario(formData).subscribe({
        next: () => {
          console.log('Usuario creado correctamente');
          this.reiniciar();
        },
        error: (err) => {
          console.error('Error al crear usuario:', err);
        }
      });*/



      this.router.navigateByUrl('/menu-admin-sistema/admins-cine');

    }
  }

  seleccionHecha(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.archivoSeleccionado = input.files[0];
    }
  }

  reiniciar(): void {
    this.nuevoRegistroForm.reset({
      TipoUsuario: TipoUsuarioEnum.USUARIO,
    });
    this.archivoSeleccionado = null;
  }

}
