import { KeyValuePipe } from '@angular/common';
import { identifierName } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormGroup, Validators, FormBuilder} from '@angular/forms';

@Component({
  selector: 'app-form-registro',
  imports: [FormsModule, ReactiveFormsModule, KeyValuePipe],
  templateUrl: './form-registro.component.html',
  styleUrl: './form-registro.component.scss'
})
export class FormRegistroComponent implements OnInit {

  nuevoRegistroFormulario!: FormGroup;
  nuevoRegistro!: Event;

    constructor(
        private formBuilder: FormBuilder
    ) 
    {

    }

  ngOnInit(): void {

    this.nuevoRegistroFormulario = this.formBuilder.group(
            {
                nombre: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
                identificacion: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
                password: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(5)]],
                confirmpassword: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(5)]],
                email: [null, [Validators.required, Validators.maxLength(150), Validators.email]],
                telefono: [null, [Validators.required, Validators.maxLength(150), Validators.pattern('^[0-9]+$')]],
                pais: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
            }
        )

  }

  submit(): void {
        if (this.nuevoRegistroFormulario.valid) {
            this.nuevoRegistro = this.nuevoRegistroFormulario.value as Event;

            /*this.eventsService.createNewEvent(this.newEvent).subscribe({
                next: () => {
                    this.reset();
                },
                error: (error: any) => {
                    console.log(error);
                }
            });*/

            console.log(this.nuevoRegistro);
        }
    }

    reset(): void {
        this.nuevoRegistroFormulario.reset({
           
        });
    }



}
