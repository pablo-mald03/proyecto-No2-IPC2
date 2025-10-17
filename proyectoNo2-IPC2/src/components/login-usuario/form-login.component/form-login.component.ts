import { KeyValuePipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLinkActive, RouterModule } from '@angular/router';
import { LoginDTO } from '../../../models/usuarios/login-dto';
import { LoginService } from '../../../services/usuarios-service/login.sercive';
import { Master } from '../../../services/masterLog/master';

@Component({
  selector: 'app-form-login',
  imports: [RouterLinkActive, RouterModule, FormsModule, ReactiveFormsModule, KeyValuePipe],
  templateUrl: './form-login.component.html',
  styleUrl: './form-login.component.scss'
})
export class FormLoginComponent implements OnInit {

  loginFormulario!: FormGroup;
  usuarioCredenciales!: LoginDTO;

  router = inject(Router);
  masterService = inject(Master);

  constructor(
    private formBuilder: FormBuilder,
    private loginService: LoginService
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

      //PENDIENTE SETEAR EL RESULTADO
      localStorage.setItem("angularUserCinema", 'pablo-01');
      this.masterService.onLogin.next(true);
      this.router.navigateByUrl("/menu-principal");
    }
  }

  reset(): void {
    this.loginFormulario.reset({

    });
  }


}
