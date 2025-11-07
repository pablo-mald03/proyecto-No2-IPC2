import { Component, OnInit } from '@angular/core';
import { CostoCineDTO } from '../../../models/cines/costo-cine-dto';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule, NgIf } from '@angular/common';
import { Popup } from '../../../shared/popup/popup';
import { FullscreenModalComponent } from '../../../shared/fullscreen-modal/fullscreen-modal.component';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-costos-cine-modificar',
  imports: [ReactiveFormsModule, CommonModule, NgIf,FullscreenModalComponent,SharedPopupComponent],
  templateUrl: './costos-cine-modificar.component.html',
  styleUrl: './costos-cine-modificar.component.scss',
  providers: [Popup]
})
export class CostosCineModificarComponent implements OnInit {


  codigoCine!: string; 

  costoForm!: FormGroup;

  costos: CostoCineDTO[] = [];


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
    private router: ActivatedRoute,
  ) { }

  //Metodo utilizado para abrir el modal
  abrirModal(mensaje: string, tipo: 'exito' | 'error' | 'info' = 'info') {
    this.mensajeModal = mensaje;
    this.tipoModal = tipo;
    this.mostrarModal = true;
  }


  //Metodo que carga al iniciar el service y permite visualizar el historial de modificaciones
  ngOnInit(): void {

    this.codigoCine = this.router.snapshot.params['codigo'];


    this.costoForm = this.fb.group({
      costo: [null, [Validators.required, Validators.min(1)]],
      fechaModificacion: [null, Validators.required]
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

  //Metodo utilizado para registrar el nuevo costo de cine
  registrarCosto() {
    if (this.costoForm.invalid) return;

    const fecha = this.costoForm.value.fechaModificacion;

    const nuevo: CostoCineDTO = {
      costo: Number(this.costoForm.value.costo),
      fechaModificacion: fecha,
      codigoCine: 'pito'
    };


    console.log(fecha);
    this.limpiarDatos();
  }

  //Metodo que permite limpiar los datos
  limpiarDatos() {
    this.costoForm.reset({
      costo: 0
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

}
