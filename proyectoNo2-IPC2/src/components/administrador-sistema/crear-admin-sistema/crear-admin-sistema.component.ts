import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { TipoUsuarioEnum } from '../../../models/usuarios/tipo-usuario-enum';
import { UsuarioService } from '../../../services/usuarios-service/usuario.service';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { UsuariosSistemaService } from '../../../services/admin-sistema-service/usuarios-sistema.service';
import { FullscreenModalComponent } from '../../../shared/fullscreen-modal/fullscreen-modal.component';
import { Popup } from '../../../shared/popup/popup';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-crear-admin-sistema',
  imports: [ReactiveFormsModule, FormsModule, RouterLink, RouterLinkActive, FullscreenModalComponent, SharedPopupComponent, NgIf],
  templateUrl: './crear-admin-sistema.component.html',
  styleUrl: './crear-admin-sistema.component.scss',
  providers: [Popup]
})
export class CrearAdminSistemaComponent implements OnInit {


  //Atributos para crear un nuevo usuario
  nuevoRegistroFormulario!: FormGroup;
  tipoUsuarioEnum = TipoUsuarioEnum;

  selectedFile!: File | null;

  router = inject(Router);

  //Atributos del modal 
  mostrarModal = false;
  mensajeModal = '';
  tipoModal: 'exito' | 'error' | 'info' = 'info';

  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;

  constructor(
    private formBuilder: FormBuilder,
    private usuarioService: UsuariosSistemaService,
    private popUp: Popup,
  ) {

  }

  //Metodo utilizado para abrir el modal
  abrirModal(mensaje: string, tipo: 'exito' | 'error' | 'info' = 'info') {
    this.mensajeModal = mensaje;
    this.tipoModal = tipo;
    this.mostrarModal = true;
  }

  ngOnInit(): void {

    this.nuevoRegistroFormulario = this.formBuilder.group(
      {
        nombre: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
        identificacion: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
        id: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
        password: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(5)]],
        confirmPassword: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(5)]],
        correo: [null, [Validators.required, Validators.maxLength(150), Validators.email, Validators.pattern(/^[a-zA-Z0-9._%+-]+@admin\.com$/)]],
        telefono: [null, [Validators.required, Validators.maxLength(150), Validators.pattern('^[0-9]+$')]],
        codigoRol: [TipoUsuarioEnum.ADMINISTRADOR_SISTEMA, Validators.required],
        pais: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
      }
    )

    this.nuevoRegistroFormulario.valueChanges.subscribe(() => {
      const pass = this.nuevoRegistroFormulario.get('password')?.value;
      const confirm = this.nuevoRegistroFormulario.get('confirmPassword')?.value;

      if (confirm && pass !== confirm) {
        this.nuevoRegistroFormulario.get('confirmPassword')?.setErrors({ mismatch: true });
      } else {
        this.nuevoRegistroFormulario.get('confirmPassword')?.setErrors(null);
      }
    });

    this.popUp.popup$.subscribe(data => {
      this.mensajePopup = data.mensaje;
      this.tipoPopup = data.tipo;

      this.mostrarPopup = false;

      setTimeout(() => {
        this.popupKey++;
        this.mostrarPopup = true;
      }, 10);

      if (data.duracion) {
        setTimeout(() => {
          this.mostrarPopup = false;
        }, data.duracion);
      }
    });

  }

  //metodo que se ejecuta para poder crear el administrador de sistema
  submit(): void {
    if (this.nuevoRegistroFormulario.valid) {

      const formData = new FormData();

      Object.entries(this.nuevoRegistroFormulario.value).forEach(([key, value]) => {
        formData.append(key, value as string);
      });


      formData.append('codigoRol', 'ADMINISTRADOR_SISTEMA');

      if (this.selectedFile) {
        formData.append('foto', this.selectedFile);
      }

      this.usuarioService.crearAdministradorSistema(formData).subscribe({
        next: () => {

          this.reiniciar();
          this.abrirModal('Se ha creado el administrador de sistema ', 'exito');
        },
        error: (err) => {
          this.mostrarError(err);
        }
      });
    }
  }

  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  //Metodo util para mostrar errors
  mostrarError(error: any): void {

    let mensaje = 'Ocurrió un error';

    if (error.error && error.error.mensaje) {
      mensaje = error.error.mensaje;
    } else if (error.message) {
      mensaje = error.message;
    }

    this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
  }

  reiniciar(): void {
    this.nuevoRegistroFormulario.reset({
      TipoUsuario: TipoUsuarioEnum.USUARIO,
    });
    this.selectedFile = null;
  }

}
