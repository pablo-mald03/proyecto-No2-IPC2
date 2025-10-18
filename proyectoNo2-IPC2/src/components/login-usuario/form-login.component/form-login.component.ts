import { KeyValuePipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLinkActive, RouterModule } from '@angular/router';
import { LoginDTO } from '../../../models/usuarios/login-dto';
import { LoginService } from '../../../services/usuarios-service/login.sercive';
import { Master } from '../../../services/masterLog/master';
import { UserLoggedDTO } from '../../../models/usuarios/user-logged-dto';
import { TipoUsuarioEnum } from '../../../models/usuarios/tipo-usuario-enum';

@Component({
  selector: 'app-form-login',
  imports: [RouterLinkActive, RouterModule, FormsModule, ReactiveFormsModule],
  templateUrl: './form-login.component.html',
  styleUrl: './form-login.component.scss'
})
export class FormLoginComponent implements OnInit {

  loginFormulario!: FormGroup;
  usuarioCredenciales!: LoginDTO;
  usuarioLogged?: UserLoggedDTO;

  router = inject(Router);
  masterService = inject(Master);

  constructor(
    private formBuilder: FormBuilder,
    private loginService: LoginService,

  ) {

  }

  ngOnInit(): void {

    localStorage.removeItem("angularUserCinema");

    this.loginFormulario = this.formBuilder.group(
      {
        correo: [null, [Validators.required, Validators.maxLength(150), Validators.email]],
        password: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(5)]],
      }
    )

  }

  submit(): void {
    if (this.loginFormulario.valid) {

      this.usuarioCredenciales = this.loginFormulario.value as LoginDTO;

      /*this.loginService.autenticarUsuario(this.usuarioCredenciales).subscribe({
        next: () => this.reset(),


        error: (error: any) => console.log(error)

        
      });*/



      this.usuarioLogged = {
        id: "tilinsin-01",
        rol: "ADMINISTRADOR DE SISTEMA"
        //rol: "USUARIO"
      }

      //PENDIENTE SETEAR EL RESULTADO
      localStorage.setItem("angularUserCinema", JSON.stringify(this.usuarioLogged));

      const usuarioStr = localStorage.getItem("angularUserCinema");

      if (usuarioStr) {
        const usuario = JSON.parse(usuarioStr) as UserLoggedDTO;

        this.masterService.onLogin.next(true);
        // Comparar como texto usando el enum
        if (usuario.rol === TipoUsuarioEnum.USUARIO) {

          this.router.navigateByUrl("/menu-principal");
        } else if (usuario.rol === TipoUsuarioEnum.ADMINISTRADOR_CINE) {

          this.router.navigateByUrl("/dashboard-admin-cine");
        } else if (usuario.rol === TipoUsuarioEnum.ADMINISTRADOR_SISTEMA) {

          this.router.navigateByUrl("/menu-admin-sistema");
        }

      }



    }
  }

  reset(): void {
    this.loginFormulario.reset({

    });
  }


}
