import { KeyValuePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLinkActive, RouterModule } from '@angular/router';

@Component({
  selector: 'app-form-login',
  imports: [RouterLinkActive, RouterModule, FormsModule, ReactiveFormsModule, KeyValuePipe],
  templateUrl: './form-login.component.html',
  styleUrl: './form-login.component.scss'
})
export class FormLoginComponent implements OnInit {

  loginFormulario!: FormGroup;
  usuarioCredenciales!: Event;

  constructor(
    private formBuilder: FormBuilder
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
      this.usuarioCredenciales = this.loginFormulario.value as Event;

      /*this.eventsService.createNewEvent(this.newEvent).subscribe({
          next: () => {
              this.reset();
          },
          error: (error: any) => {
              console.log(error);
          }

 i    f (this.nuevoRegistroFormulario.valid) {


      this.nuevoRegistro = this.nuevoRegistroFormulario.value as Usuario;

      this.usuarioService.crearNuevoUsuario(this.nuevoRegistro).subscribe({
        next: () => this.reset(),
        error: (error: any) => console.log(error)
      });

      console.log(this.nuevoRegistro);

      
    }


      });*/

      console.log(this.usuarioCredenciales);
    }
  }

  reset(): void {
    this.loginFormulario.reset({

    });
  }


}
