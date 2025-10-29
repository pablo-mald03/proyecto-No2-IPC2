import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-reestablecer-credenciales',
  imports: [FormsModule, CommonModule,ReactiveFormsModule],
  templateUrl: './reestablecer-credenciales.component.html',
  styleUrl: './reestablecer-credenciales.component.scss'
})
export class ReestablecerCredencialesComponent implements OnInit {

  reestablecerForm!: FormGroup;
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: string = 'success';


  constructor(private fb: FormBuilder) {

  }

  //Muestra todo el form al inicializarse
  ngOnInit(): void {

    this.reestablecerForm = this.fb.group(
      {
        correo: ['', [Validators.required, Validators.email]],
        idUsuario: ['', [Validators.required, Validators.minLength(3)]],
        nuevaPassword: ['', [Validators.required, Validators.minLength(5)]],
        confirmarPassword: ['', [Validators.required, Validators.minLength(5)]],
      },
      { validators: this.passwordsIgualesValidator }
    );

  }

  private passwordsIgualesValidator(formGroup: AbstractControl) {
    const password = formGroup.get('nuevaPassword')?.value;
    const confirm = formGroup.get('confirmarPassword')?.value;

    if (password && confirm && password !== confirm) {
      formGroup.get('confirmarPassword')?.setErrors({ mismatch: true });
    } else {
      // elimina el error si antes existía
      const errors = formGroup.get('confirmarPassword')?.errors;
      if (errors) {
        delete errors['mismatch'];
        if (Object.keys(errors).length === 0) {
          formGroup.get('confirmarPassword')?.setErrors(null);
        }
      }
    }
    return null;




  }




  reiniciar() {
    this.reestablecerForm.reset();
  }




  submit() {
    if (this.reestablecerForm.invalid) {
      this.reestablecerForm.markAllAsTouched();
      return;
    }

    // simulación de proceso de reestablecer contraseña
    const valores = this.reestablecerForm.value;
    console.log('Formulario enviado:', valores);

    this.mostrarPopup = true;
    this.mensajePopup = 'Tu contraseña ha sido actualizada correctamente.';
    this.tipoPopup = 'success';

    this.reiniciar();




  }

}
