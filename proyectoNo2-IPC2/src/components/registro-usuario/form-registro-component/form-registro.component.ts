import { KeyValuePipe, NgFor, NgIf } from '@angular/common';
import { identifierName } from '@angular/compiler';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormGroup, Validators, FormBuilder } from '@angular/forms';
import { Usuario } from '../../../models/usuarios/usuario';
import { UsuarioService } from '../../../services/usuarios-service/usuario.service';
import { TipoUsuarioEnum } from '../../../models/usuarios/tipo-usuario-enum';
import { Popup } from '../../../shared/popup/popup';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';

@Component({
  selector: 'app-form-registro',
  imports: [FormsModule, ReactiveFormsModule, NgFor, NgIf, SharedPopupComponent],
  templateUrl: './form-registro.component.html',
  styleUrl: './form-registro.component.scss',
  providers: [Popup]
})
export class FormRegistroComponent implements OnInit {

  nuevoRegistroFormulario!: FormGroup;
  nuevoRegistro!: Usuario;
  tipoUsuarioEnums = TipoUsuarioEnum;

  //Indica el archivo seleccionado
  selectedFile!: File | null;

  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;

  constructor(
    private formBuilder: FormBuilder,
    private usuarioService: UsuarioService,
    private popUp: Popup,

  ) {

  }

  ngOnInit(): void {

    this.nuevoRegistroFormulario = this.formBuilder.group(
      {
        nombre: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
        identificacion: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
        id: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
        password: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(5)]],
        confirmpassword: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(5)]],
        correo: [null, [Validators.required, Validators.maxLength(150), Validators.email]],
        telefono: [null, [Validators.required, Validators.maxLength(150), Validators.pattern('^[0-9]+$')]],
        codigoRol: [TipoUsuarioEnum.USUARIO, Validators.required],
        pais: [null, [Validators.required, Validators.maxLength(150), Validators.minLength(2)]],
      }
    )

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

  submit(): void {
    if (this.nuevoRegistroFormulario.valid) {


      console.log(this.nuevoRegistro);


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

          let mensaje = 'Te has registrado correctamente';

          this.popUp.mostrarPopup({ mensaje, tipo: 'exito' });
          this.reiniciar();
        },
        error: (error) => {
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

  //Metodo encargado de recibir el 
  onFileChange(event: Event): void {
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

  @ViewChild('fotoInput') fotoInput!: ElementRef<HTMLInputElement>;

  //Metodo utilizado para reiniciar cuando se inserta
  reiniciar(): void {
    this.nuevoRegistroFormulario.reset({
      codigoRol: TipoUsuarioEnum.USUARIO,
    });
    this.selectedFile = null;


    if (this.fotoInput) {
      this.fotoInput.nativeElement.value = '';
    }
  }

}
