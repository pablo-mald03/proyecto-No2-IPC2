import { CommonModule, NgClass, NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TipoAnuncioEnum } from '../../../models/anuncios/tipo-anuncios-enum';
import { Anuncio } from '../../../models/anuncios/anuncio';
import { Popup } from '../../../shared/popup/popup';
import { SharedPopupComponent } from "../../pop-ups/shared-popup.component/shared-popup.component";

@Component({
  selector: 'app-comprar-anuncio',
  imports: [ReactiveFormsModule, CommonModule, NgFor, SharedPopupComponent, NgIf],
  templateUrl: './comprar-anuncio.component.html',
  styleUrl: './comprar-anuncio.component.scss',
  providers: [Popup],
})
export class ComprarAnuncioComponent implements OnInit {


  anuncioForm!: FormGroup;
  pagoForm!: FormGroup;


  tipoSeleccionado: number | null = null;
  tiposAnuncio: { codigo: number, label: string }[] = [];

  imagenFile: File | null = null;


  //Atributos para mostrar el popup cuando haya un error o informacion
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;

  // Indica si se debe mostrar el segundo formulario
  mostrarSegundoFormulario = false;


  // Aquí guardaremos el FormData para completarlo después
  formDataAnuncio!: FormData;

  constructor(
    private fb: FormBuilder,
    private popUp: Popup,
    private formBuilder: FormBuilder,) { }

  ngOnInit(): void {
    // Cargar los tipos de anuncio desde el enum
    this.tiposAnuncio = [
      { codigo: 1, label: TipoAnuncioEnum.ANUNCIO_TEXTO },
      { codigo: 2, label: TipoAnuncioEnum.IMAGEN_TEXTO },
      { codigo: 3, label: TipoAnuncioEnum.VIDEO_TEXTO },
    ];

    this.pagoForm = this.formBuilder.group({
      metodo: ['', Validators.required],
      monto: [{ value: 0, disabled: true }]
    });

    // Crear formulario reactivo
    this.anuncioForm = this.fb.group({
      codigo: [''],
      estado: [true],
      nombre: ['', Validators.required],
      caducacion: [false],
      fechaExpiracion: ['', Validators.required],
      fechaCompra: [new Date()],
      url: [''],
      texto: ['', Validators.required],
      foto: [''],
      codigoTipo: [1, Validators.required],
      idUsuario: [''],
    });

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

    // Detectar cambio en tipo seleccionado
    this.anuncioForm.get('codigoTipo')?.valueChanges.subscribe(valor => {
      this.tipoSeleccionado = valor;
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.imagenFile = file;

      const reader = new FileReader();
      reader.onload = () => {
        this.anuncioForm.patchValue({ foto: reader.result as string });
      };
      reader.readAsDataURL(file);
    }
  }

  // Enviar primer formulario
  submit() {
    if (this.anuncioForm.invalid) {
      this.anuncioForm.markAllAsTouched();
      return;
    }

    // Guardamos el formData inicial
    this.formDataAnuncio = new FormData();

    this.formDataAnuncio.append('nombre', this.anuncioForm.value.nombre);
    this.formDataAnuncio.append('codigoTipo', this.anuncioForm.value.codigoTipo);
    this.formDataAnuncio.append('texto', this.anuncioForm.value.texto);
    this.formDataAnuncio.append('url', this.anuncioForm.value.url);
    this.formDataAnuncio.append('fechaExpiracion', this.anuncioForm.value.fechaExpiracion);

    if (this.imagenFile) {
      this.formDataAnuncio.append('foto', this.imagenFile);
    }

    console.log('FormData anuncio armado!');

    // Ahora sí se muestra el segundo form
    this.mostrarSegundoFormulario = true;
  }


  //Metodo que permite limpiar el formulario
   limpiar() {
    this.anuncioForm.reset({
      estado: true,
      codigoTipo: 1,
      fechaCompra: new Date()
    });
    this.imagenFile = null;
  }

  //Metodo implementado para mostrar los mensajes de errores
  mostrarError(errorEncontrado: any) {
    let mensaje = 'Ocurrió un error';

    if (errorEncontrado.error && errorEncontrado.error.mensaje) {
      mensaje = errorEncontrado.error.mensaje;
    } else if (errorEncontrado.message) {
      mensaje = errorEncontrado.message;
    }

    this.popUp.mostrarPopup({ mensaje, tipo: 'error' });

  }

  //Metodo que permite confirmar el pago que se hara
  confirmarPago() {
    if (this.pagoForm.invalid) {
      this.pagoForm.markAllAsTouched();
      return;
    }

    // Complementamos el FormData existente
    this.formDataAnuncio.append('metodoPago', this.pagoForm.value.metodo);
    this.formDataAnuncio.append('monto', this.pagoForm.getRawValue().monto);

    console.log('FormData completo listo para enviar:', this.formDataAnuncio);

    // Ahora sí mandas TODO con tu servicio
    // this.miServicio.enviarTodo(this.formDataAnuncio).subscribe(...)
  }

}
