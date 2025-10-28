import { KeyValuePipe, NgIf } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLinkActive, RouterModule } from '@angular/router';
import { LoginDTO } from '../../../models/usuarios/login-dto';
import { LoginService } from '../../../services/usuarios-service/login.sercive';
import { Master } from '../../../services/masterLog/master';
import { UserLoggedDTO } from '../../../models/usuarios/user-logged-dto';
import { TipoUsuarioEnum } from '../../../models/usuarios/tipo-usuario-enum';
import { Popup } from '../../../shared/popup/popup';
import { SharedPopupComponent } from "../../pop-ups/shared-popup.component/shared-popup.component";

@Component({
  selector: 'app-form-login',
  imports: [RouterLinkActive, RouterModule, FormsModule, ReactiveFormsModule, SharedPopupComponent, NgIf],
  templateUrl: './form-login.component.html',
  styleUrl: './form-login.component.scss',
  providers: [Popup]
})
export class FormLoginComponent implements OnInit {

  loginFormulario!: FormGroup;
  usuarioCredenciales!: LoginDTO;
  usuarioLogged?: UserLoggedDTO;

  router = inject(Router);
  masterService = inject(Master);

  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';

  constructor(
    private formBuilder: FormBuilder,
    private loginService: LoginService,
    private popUp: Popup,

  ) {

  }

  popupKey = 0;

  ngOnInit(): void {

    localStorage.removeItem("angularUserCinema");

    this.loginFormulario = this.formBuilder.group(
      {
        correo: [null, [Validators.required, Validators.maxLength(150), Validators.email]],
        password: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(5)]],
      }
    )

    this.popUp.popup$.subscribe(data => {
      // Se actualiza el contenido del popup
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

  submit(): void {
    if (this.loginFormulario.valid) {

      this.usuarioCredenciales = this.loginFormulario.value as LoginDTO;

      this.loginService.autenticarUsuario(this.usuarioCredenciales).subscribe({
        next: (response: any) => {

          this.usuarioLogged = {

            id: response.id,
            rol: TipoUsuarioEnum[response.rol as keyof typeof TipoUsuarioEnum]
          };

          this.reset();

          this.ingresarMenu();

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
  }

  //Metodo que sirve para ingresar al menu principal
  private ingresarMenu() {

    //Se setea el token retornado por la api
    localStorage.setItem("angularUserCinema", JSON.stringify(this.usuarioLogged));

    const usuarioStr = localStorage.getItem("angularUserCinema");

    if (usuarioStr) {
      const usuario = JSON.parse(usuarioStr) as UserLoggedDTO;

      this.masterService.onLogin.next(true);
      // Comparar como texto usando el enum
      if (usuario.rol === TipoUsuarioEnum.USUARIO) {

        this.router.navigateByUrl("/menu-principal");
      }
      else if (usuario.rol === TipoUsuarioEnum.ADMINISTRADOR_CINE) {

        this.router.navigateByUrl("/menu-admin-cine");
      }
      else if (usuario.rol === TipoUsuarioEnum.USUARIO_ESPECIAL) {

        this.router.navigateByUrl("/menu-anunciante");
      }
      else if (usuario.rol === TipoUsuarioEnum.ADMINISTRADOR_SISTEMA) {

        this.router.navigateByUrl("/menu-admin-sistema");
      }

    }

  }

  reset(): void {
    this.loginFormulario.reset({

    });
  }


}
