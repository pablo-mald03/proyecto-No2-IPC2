import { CommonModule, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuarioService } from '../../../services/usuarios-service/usuario.service';
import { CambioCredenciales } from '../../../models/usuarios/cambio-credenciales';
import { Popup } from '../../../shared/popup/popup';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';

@Component({
  selector: 'app-reestablecer-credenciales',
  imports: [FormsModule, CommonModule, ReactiveFormsModule, NgIf, SharedPopupComponent],
  templateUrl: './reestablecer-credenciales.component.html',
  styleUrl: './reestablecer-credenciales.component.scss',
  providers: [Popup]
})
export class ReestablecerCredencialesComponent implements OnInit {

  reestablecerForm!: FormGroup;
  cambioCredenciales!: CambioCredenciales;

  //Atributos que permiten mostrar el modal
  mostrarModal: boolean = false;

  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';

  //Llave que le permite a angular saber cuando cerrar el form
  popupKey = 0;


  constructor(
    private fb: FormBuilder,
    private router: Router,
    private usuarioService: UsuarioService,
    private popUp: Popup) {

  }




  //Muestra todo el form al inicializarse
  ngOnInit(): void {

    this.reestablecerForm = this.fb.group(
      {
        correo: ['', [Validators.required, Validators.email]],
        idUsuario: ['', [Validators.required, Validators.minLength(3)]],
        password: ['', [Validators.required, Validators.minLength(5)]],
        passwordConfirm: ['', [Validators.required, Validators.minLength(5)]],
      },
      { validators: this.passwordsIgualesValidator }
    );

    //Permite visualizar el servicio del popUp cuantas veces se desee
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

  //Metodo que sirve para saber si las password de confirmacion son iguales
  private passwordsIgualesValidator(formGroup: AbstractControl) {
    const password = formGroup.get('password')?.value;
    const confirm = formGroup.get('passwordConfirm')?.value;

    if (password && confirm && password !== confirm) {
      formGroup.get('passwordConfirm')?.setErrors({ mismatch: true });
    } else {
      // elimina el error si antes existía
      const errors = formGroup.get('passwordConfirm')?.errors;
      if (errors) {
        delete errors['mismatch'];
        if (Object.keys(errors).length === 0) {
          formGroup.get('passwordConfirm')?.setErrors(null);
        }
      }
    }
    return null;

  }


  //Metodo utilizado para reiniciar el formulario cuando se haga el submit
  reiniciar() {
    this.reestablecerForm.reset();
  }

  //Metodo utilizado para cerrar el modal
  cerrarModal() {
    this.mostrarModal = false;
  }


  //Metodo que sirve para enviar la request de cambio de password
  submit() {
    if (this.reestablecerForm.invalid) {
      this.reestablecerForm.markAllAsTouched();
      return;
    }

    this.cambioCredenciales = this.reestablecerForm.value as CambioCredenciales;

    this.usuarioService.cambiarCredenciales(this.cambioCredenciales).subscribe({
      next: (response: any) => {

        this.mostrarModal = true;
        this.reiniciar();

      },
      error: (error: any) => {

        let mensaje = 'Ocurrió un error';

        if (error.error && error.error.mensaje) {
          mensaje = error.error.mensaje;
        } else if (error.message) {
          mensaje = error.message;
        }

        this.popUp.mostrarPopup({ mensaje, tipo: 'error' });

      }
    });


  }

  private async simularServicioReestablecer(): Promise<boolean> {
    return new Promise(resolve => setTimeout(() => resolve(true), 600));
  }

  //Metodo que sirve para redirigir al login
  irLogin() {
    this.mostrarModal = false;
    this.router.navigateByUrl('/login');
  }

}
