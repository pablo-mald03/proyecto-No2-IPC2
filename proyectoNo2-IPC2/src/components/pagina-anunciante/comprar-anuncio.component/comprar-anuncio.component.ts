import { NgClass, NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-comprar-anuncio',
  imports: [ReactiveFormsModule, NgClass, NgIf, NgFor],
  templateUrl: './comprar-anuncio.component.html',
  styleUrl: './comprar-anuncio.component.scss'
})
export class ComprarAnuncioComponent implements OnInit {

  anuncioForm!: FormGroup;

  //PENDIENTE TERMINAR ESTA BASURA

  //Tipos de anuncio que existen
  tiposAnuncio = [

    { codigo: 1, label: 'ANUNCIO DE TEXTO' },
    { codigo: 2, label: 'ANUNCIO DE TEXTO E IMAGEN' },
    { codigo: 3, label: 'ANUNCIO DE VIDEO Y TEXTO' }

  ]


  constructor(
    private formBuilder: FormBuilder

  ) {

  }

  tipoSeleccionado!: number;

  ngOnInit(): void {
    this.anuncioForm = this.formBuilder.group({
      nombre: ['', [Validators.required]],
      codigoTipo: [1, [Validators.required]],
      texto: [''],
      url: [''],
      foto: [null],
      fechaExpiracion: ['', [Validators.required]]
    });

    this.tipoSeleccionado = this.anuncioForm.get('codigoTipo')?.value;

    this.anuncioForm.get('codigoTipo')?.valueChanges.subscribe(tipoSeleccionado => {
      this.tipoSeleccionado = tipoSeleccionado;
      this.resetCamposPorTipo(tipoSeleccionado);
    });
  }
  //Metodo que ayuda a darle dinamismo al formulario para irlo acoplando a lo que elija el usuario
  resetCamposPorTipo(tipo: number) {
    if (tipo === 1) {
      this.anuncioForm.patchValue({ texto: '', url: '', foto: null }, { emitEvent: false });
    } else if (tipo === 2) {
      this.anuncioForm.patchValue({ texto: '', url: '', foto: null }, { emitEvent: false });
    } else if (tipo === 3) {
      this.anuncioForm.patchValue({ texto: '', url: '', foto: null }, { emitEvent: false });
    }

  }

  //Metodo que sirve para detectar si el usuario subio una imagen
  onFileSelected(event: any) {
    const file = event.target.files[0];
    this.anuncioForm.patchValue({ foto: file });
  }

  //Metodo para enviar el formulario
  submit() {

    if (this.anuncioForm.valid) {

      console.log('Formulario enviado:', this.anuncioForm.value);


    }

  }

  //Metodo que sirve para saber el tipo de anuncio que se va a seleccionar
  getTipoAnuncio(codigo: number): string {
    switch (codigo) {
      case 1: return 'ANUNCIO DE TEXTO';
      case 2: return 'ANUNCIO DE TEXTO E IMAGEN';
      case 3: return 'ANUNCIO DE VIDEO Y TEXTO';
      default: return 'DESCONOCIDO';
    }
  }

}
