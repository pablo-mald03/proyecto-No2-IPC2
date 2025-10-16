import { KeyValuePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLinkActive, RouterModule } from '@angular/router';
import { LoginDTO } from '../../../models/usuarios/login-dto';
import { LoginService } from '../../../services/usuarios-service/login.sercive';

@Component({
  selector: 'app-form-login',
  imports: [RouterLinkActive, RouterModule, FormsModule, ReactiveFormsModule, KeyValuePipe],
  templateUrl: './form-login.component.html',
  styleUrl: './form-login.component.scss'
})
export class FormLoginComponent implements OnInit {

  loginFormulario!: FormGroup;
  usuarioCredenciales!: LoginDTO;

  constructor(
    private formBuilder: FormBuilder,
    private loginService: LoginService
  ) {

  }

  ngOnInit(): void {

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

      this.loginService.autenticarUsuario(this.usuarioCredenciales).subscribe({
        next: () => this.reset(),
        error: (error: any) => console.log(error)
      });
    }
  }

  reset(): void {
    this.loginFormulario.reset({

    });
  }


}
