import { CommonModule, NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TipoAnuncioEnum } from '../../../models/anuncios/tipo-anuncios-enum';
import { Anuncio } from '../../../models/anuncios/anuncio';

@Component({
  selector: 'app-comprar-anuncio',
  imports: [ReactiveFormsModule, CommonModule, NgFor],
  templateUrl: './comprar-anuncio.component.html',
  styleUrl: './comprar-anuncio.component.scss'
})
export class ComprarAnuncioComponent implements OnInit {

   anuncioForm!: FormGroup;
  tipoSeleccionado: number | null = null;
  tiposAnuncio: { codigo: number, label: string }[] = [];

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    // Cargar los tipos de anuncio desde el enum
    this.tiposAnuncio = [
      { codigo: 1, label: TipoAnuncioEnum.ANUNCIO_TEXTO },
      { codigo: 2, label: TipoAnuncioEnum.IMAGEN_TEXTO },
      { codigo: 3, label: TipoAnuncioEnum.VIDEO_TEXTO },
    ];

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

    // Detectar cambio en tipo seleccionado
    this.anuncioForm.get('codigoTipo')?.valueChanges.subscribe((valor) => {
      this.tipoSeleccionado = valor;
      console.log(this.tipoSeleccionado);
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.anuncioForm.patchValue({ foto: reader.result as string });
      };
      reader.readAsDataURL(file);
    }
  }

  submit(): void {
    if (this.anuncioForm.valid) {

      const nuevoAnuncio: Anuncio = this.anuncioForm.value;
      console.log('📢 Anuncio creado:', nuevoAnuncio);
      // Aquí puedes llamar a tu servicio para enviar al backend

    } else {
      this.anuncioForm.markAllAsTouched();
    }
  }


  limpiar(){
    this.anuncioForm.reset({


    })
  }

}
