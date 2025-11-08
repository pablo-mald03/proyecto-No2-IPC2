import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EditarCineDTO } from '../../../models/cines/editar-cine-dto';
import { CommonModule, NgIf } from '@angular/common';
import { SharedPopupComponent } from '../../pop-ups/shared-popup.component/shared-popup.component';
import { FullscreenModalComponent } from '../../../shared/fullscreen-modal/fullscreen-modal.component';
import { Popup } from '../../../shared/popup/popup';
import { ActivatedRoute, Router } from '@angular/router';
import { CineService } from '../../../services/cine-service/cine.service';

@Component({
  selector: 'app-editar-cine',
  imports: [ReactiveFormsModule, CommonModule, SharedPopupComponent, FullscreenModalComponent, NgIf],
  templateUrl: './editar-cine.component.html',
  styleUrl: './editar-cine.component.scss',
  providers: [Popup]
})
export class EditarCineComponent implements OnInit {

  codigoCine!: string;

  cineForm!: FormGroup;

  //Atributos del modal 
  mostrarModal = false;
  mensajeModal = '';
  urlRedireccion = '/menu-admin-sistema/cines';
  tipoModal: 'exito' | 'error' | 'info' = 'info';

  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;

  //Guarda la instancia recibida de backend
  cine!: EditarCineDTO;


  constructor(private formBuilder: FormBuilder,
    private popUp: Popup,
    private router: ActivatedRoute,
    private cineService: CineService,
    private routerBack: Router,
  ) { }

  //Se cargan los datos solicitados
  ngOnInit(): void {

    this.codigoCine = this.router.snapshot.params['codigo'];


    this.cineService.cinePorCodigo(this.codigoCine).subscribe({
      next: (response: EditarCineDTO) => {

        this.cine = response;

      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });

    this.cineForm = this.formBuilder.group({
      nombre: [this.cine.nombre, [Validators.required, Validators.minLength(3)]],
      montoOcultacion: [this.cine.montoOcultacion, [Validators.required, Validators.min(0)]],
      descripcion: [this.cine.descripcion, [Validators.required, Validators.minLength(5)]],
      ubicacion: [this.cine.ubicacion, [Validators.required, Validators.minLength(3)]],
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

  guardarCambios() {
    if (this.cineForm.invalid) return;

    const form = this.cineForm.value;

    const dto: EditarCineDTO = {
      codigo: this.cine.codigo,
      nombre: form.nombre,
      montoOcultacion: form.montoOcultacion,
      descripcion: form.descripcion,
      ubicacion: form.ubicacion
    };

    console.log('DTO listo para enviar:', dto);

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


  //Metodo que permite regresar
  regresar() {

    this.routerBack.navigateByUrl('/menu-admin-sistema/cines');
  }

}
