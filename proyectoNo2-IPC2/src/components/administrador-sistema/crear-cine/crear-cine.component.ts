import { NgClass, NgIf } from '@angular/common';
import { Component, inject, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Popup } from '../../../shared/popup/popup';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';
import { FullscreenModalComponent } from '../../../shared/fullscreen-modal/fullscreen-modal.component';
import { Cine } from '../../../models/cines/cine';
import { CineService } from '../../../services/cine-service/cine.service';

@Component({
  selector: 'app-crear-cine',
  imports: [ReactiveFormsModule, SharedPopupComponent, NgIf, FullscreenModalComponent],
  templateUrl: './crear-cine.component.html',
  styleUrl: './crear-cine.component.scss',
  providers: [Popup]
})
export class CrearCineComponent implements OnInit {


  cineForm!: FormGroup;


  router = inject(Router);


  //Atributos del modal 
  mostrarModal = false;
  mensajeModal = '';
  tipoModal: 'exito' | 'error' | 'info' = 'info';

  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;


  constructor(

    private fb: FormBuilder,
    private popUp: Popup,
    private cineService: CineService,
  ) {

  }

  //Metodo utilizado para abrir el modal
  abrirModal(mensaje: string, tipo: 'exito' | 'error' | 'info' = 'info') {
    this.mensajeModal = mensaje;
    this.tipoModal = tipo;
    this.mostrarModal = true;
  }


  ngOnInit(): void {

    this.cineForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      descripcion: ['', [Validators.required]],
      ubicacion: ['', [Validators.required]],
      montoOcultacion: [0, [Validators.required, Validators.min(0)]],
      fechaCreacion: [null, [Validators.required]],
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

  //Boton que sirve para regresar a consultar los datos de cines
  volver() {
    this.router.navigateByUrl('/menu-admin-sistema/cines');
  }

  //Metodo que permite registrar un nuevo cine
  crearCine() {
    if (this.cineForm.invalid) return;

    const fecha = this.cineForm.value.fechaCreacion; 

    const nuevoCine: Cine = {
      codigo: 'NULL',
      nombre: this.cineForm.value.nombre!,
      descripcion: this.cineForm.value.descripcion!,
      ubicacion: this.cineForm.value.ubicacion!,
      estadoAnuncio: true,
      montoOcultacion: Number(this.cineForm.value.montoOcultacion),
      fechaCreacion: fecha,
   
    };

     this.cineService.crearNuevoCine(nuevoCine).subscribe({
        next: () => {

          this.limpiarDatos();
          this.abrirModal('Se ha creado correctamente el nuevo cine', 'exito');
        },
        error: (err) => {
          this.mostrarError(err);
        }
      });

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


  //Metodo util para poder limpiar los datos
  limpiarDatos() {
    this.cineForm.reset({
      montoOcultacion: 0
    });
  }

}
