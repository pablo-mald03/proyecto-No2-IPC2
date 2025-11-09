import { CommonModule, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PeliculaSistemaService } from '../../../services/peliculas-service/peliculas-sistema.service';
import { Popup } from '../../../shared/popup/popup';
import { FullscreenModalComponent } from "../../../shared/fullscreen-modal/fullscreen-modal.component";
import { SharedPopupComponent } from "../../pop-ups/shared-popup.component/shared-popup.component";

@Component({
  selector: 'app-crear-peliculas-cine',
  imports: [ReactiveFormsModule, CommonModule, NgIf, FullscreenModalComponent, SharedPopupComponent],
  templateUrl: './crear-peliculas-cine.component.html',
  styleUrl: './crear-peliculas-cine.component.scss',
  providers: [Popup],
})
export class CrearPeliculasCineComponent implements OnInit {

  peliculaForm!: FormGroup;
  posterPreview: string | ArrayBuffer | null = null;
  archivoPoster: File | null = null;


  //Atributos del modal 
  mostrarModal = false;
  mensajeModal = '';
  urlRedireccion = '/menu-admin-sistema/peliculas';
  tipoModal: 'exito' | 'error' | 'info' = 'info';

  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;

  //Metodo utilizado para abrir el modal
  abrirModal(mensaje: string, tipo: 'exito' | 'error' | 'info' = 'info') {
    this.mensajeModal = mensaje;
    this.tipoModal = tipo;
    this.mostrarModal = true;
  }




  constructor(
    private fb: FormBuilder,
    private peliculasService: PeliculaSistemaService,
    private popUp: Popup,
  ) {

  }
  ngOnInit(): void {


    this.peliculaForm = this.fb.group({
      nombre: ['', [Validators.required]],
      sinopsis: ['', [Validators.required, Validators.minLength(10)]],
      cast: ['', [Validators.required]],
      director: ['', [Validators.required]],
      fechaEstreno: ['', [Validators.required]],
      clasificacion: ['', [Validators.required]],
      duracion: ['', [Validators.required]],
      precio: [null, [Validators.required, Validators.min(0)]]
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
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (!file) return;

    this.archivoPoster = file;

    const reader = new FileReader();
    reader.onload = () => {
      this.posterPreview = reader.result;
    };
    reader.readAsDataURL(file);
  }



  //Metodo util para mostrar errors
  mostrarError(error: any): void {

    let mensaje = 'Ocurrió un error';

    if (error.error && error.error.mensaje) {
      mensaje = error.error.mensaje;
    } else if (error.message) {
      mensaje = error.message;
    }

    this.popUp.mostrarPopup({ mensaje, tipo: 'error' });
  }


  crearPelicula(): void {
    if (this.peliculaForm.invalid || !this.archivoPoster) {
      console.warn('Formulario inválido o sin poster');
      return;
    }

    const formData = new FormData();

    Object.keys(this.peliculaForm.controls).forEach(key => {
      formData.append(key, this.peliculaForm.get(key)?.value);
    });

    formData.append('poster', this.archivoPoster, this.archivoPoster.name);

    this.peliculasService.crearPelicula(formData).subscribe({
      next: (resp) => {

        this.peliculaForm.reset();
        this.posterPreview = null;
        this.archivoPoster = null;
        this.abrirModal('Se ha creado correctamente la nueva pelicula', 'exito');


      },
      error: (err: any) => {
        this.mostrarError(err);
      }
    });
  }

  cancelar(): void {
    this.peliculaForm.reset();
    this.posterPreview = null;
    this.archivoPoster = null;
  }

}
