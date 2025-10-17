import { KeyValuePipe, NgFor } from '@angular/common';
import { identifierName } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormGroup, Validators, FormBuilder } from '@angular/forms';
import { Usuario } from '../../../models/usuarios/usuario';
import { UsuarioService } from '../../../services/usuarios-service/usuario.service';
import { TipoUsuarioEnum } from '../../../models/usuarios/tipo-usuario-enum';

@Component({
  selector: 'app-form-registro',
  imports: [FormsModule, ReactiveFormsModule, NgFor],
  templateUrl: './form-registro.component.html',
  styleUrl: './form-registro.component.scss'
})
export class FormRegistroComponent implements OnInit {

  nuevoRegistroFormulario!: FormGroup;
  nuevoRegistro!: Usuario;
  TipoUsuarioEnums = TipoUsuarioEnum;

  //Indica el archivo seleccionado
  selectedFile!: File | null;

  constructor(
    private formBuilder: FormBuilder,
    private usuarioService: UsuarioService
  ) {

  }

  ngOnInit(): void {

    this.nuevoRegistroFormulario = this.formBuilder.group(
      {
        nombre: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
        identificacion: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
        userid: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
        password: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(5)]],
        confirmpassword: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(5)]],
        email: [null, [Validators.required, Validators.maxLength(150), Validators.email]],
        telefono: [null, [Validators.required, Validators.maxLength(150), Validators.pattern('^[0-9]+$')]],
        TipoUsuario: [TipoUsuarioEnum.USUARIO, Validators.required],
        pais: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
      }
    )

  }

  submit(): void {
    if (this.nuevoRegistroFormulario.valid) {

      this.nuevoRegistro = this.nuevoRegistroFormulario.value as Usuario;

      this.usuarioService.crearNuevoUsuario(this.nuevoRegistro).subscribe({
        next: () => this.reset(),
        error: (error: any) => console.log(error)
      });

      console.log(this.nuevoRegistro);

      /*
       const formData = new FormData();

      // Agregar todos los campos del formulario
      Object.entries(this.nuevoRegistroFormulario.value).forEach(([key, value]) => {
        formData.append(key, value as string);
      });

      // Agregar el archivo (si hay)
      if (this.selectedFile) {
        formData.append('foto', this.selectedFile);
      }

      // Enviar al servicio
      this.usuarioService.crearNuevoUsuario(formData).subscribe({
        next: () => {
          console.log('Usuario creado correctamente');
          this.reset();
        },
        error: (err) => {
          console.error('Error al crear usuario:', err);
        } 
      */


    }
  }

  //Metodo encargado de recibir el 
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  TiposuarioOptions = Object.keys(TipoUsuarioEnum)
    .filter(val => ['USUARIO', 'USUARIO_ESPECIAL'].includes(val))
    .map(key => ({
      key: key,
      value: TipoUsuarioEnum[key as keyof typeof TipoUsuarioEnum]
    }));

  reset(): void {
    this.nuevoRegistroFormulario.reset({
      TipoUsuario: TipoUsuarioEnum.USUARIO,
    });
    this.selectedFile = null;
  }





}
