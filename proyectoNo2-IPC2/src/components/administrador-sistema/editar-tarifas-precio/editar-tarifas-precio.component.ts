import { CommonModule, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { VigenciaAnuncio } from '../../../models/anuncios/vigencia-anuncio';
import { ActivatedRoute, Router } from '@angular/router';
import { VigenciaAnunciosService } from '../../../services/anuncios-service/vigencia-anuncios.service';
import { CambiarPrecioDTO } from '../../../models/anuncios/cambiar-precio-dto';
import { Popup } from '../../../shared/popup/popup';
import { FullscreenModalComponent } from "../../../shared/fullscreen-modal/fullscreen-modal.component";
import { SharedPopupComponent } from "../../pop-ups/shared-popup.component/shared-popup.component";

@Component({
  selector: 'app-editar-tarifas-precio',
  imports: [NgIf, CommonModule, ReactiveFormsModule, FullscreenModalComponent, SharedPopupComponent],
  templateUrl: './editar-tarifas-precio.component.html',
  styleUrl: './editar-tarifas-precio.component.scss',
  providers: [Popup],
})
export class EditarTarifasPrecioComponent {


  formVigencia!: FormGroup;

  //Se guarda la referencia de vigencia
  vigencia!: VigenciaAnuncio;
  codigo!: number;

  //Atributos del modal 
  mostrarModal = false;
  mensajeModal = '';
  urlRedireccion = '/menu-admin-sistema/costos/tarifas';
  tipoModal: 'exito' | 'error' | 'info' = 'info';

  //Atributos para mostrar el popup cuando haya un error
  mostrarPopup: boolean = false;
  mensajePopup: string = '';
  tipoPopup: 'error' | 'exito' | 'info' = 'info';
  popupKey = 0;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private vigenciaService: VigenciaAnunciosService,
    private router: Router,
    private popUp: Popup,
  ) { }

  //Metodo que permite cargar los datos provinientes del service
  ngOnInit(): void {
    this.codigo = this.route.snapshot.params['id'];


    this.popUp.popup$.subscribe(data => {

      this.mensajePopup = data.mensaje;
      this.tipoPopup = data.tipo;

      this.mostrarPopup = false;

      setTimeout(() => {
        this.popupKey++;
        this.mostrarPopup = true;
      }, 10);

      if (data.duracion) {
        setTimeout(() => this.mostrarPopup = false, data.duracion);
      }
    });

    this.vigenciaService.vigenciaAnuncioCodigo(this.codigo).subscribe({
      next: (data: VigenciaAnuncio) => {
        this.vigencia = data;

        this.formVigencia = this.fb.group({
          precio: [this.vigencia.precio, [Validators.required, Validators.min(1)]],
        });
      },
      error: (error) => {
        this.mostrarError(error);
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


  //Metodo que permite modificar el precio de la tarifa
  guardarCambios() {
    if (this.formVigencia.invalid) return;

    const dto: CambiarPrecioDTO = {
      codigo: this.vigencia.codigo,
      precio: this.formVigencia.value.precio,
    };

    this.vigenciaService.cambiarPrecio(dto).subscribe({
      next: () => {
        this.abrirModal(
          `Se ha editado correctamente la tarifa de ${this.vigencia.contexto}`,
          'exito'
        );
      },
      error: (err) => {
        this.mostrarError(err);
      }
    });
  }

  //Metodo que permite regresar
  regresar() {
    this.router.navigateByUrl('/menu-admin-sistema/costos/tarifas');
  }

  //Metodo utilizado para abrir el modal
  abrirModal(mensaje: string, tipo: 'exito' | 'error' | 'info' = 'info') {
    this.mensajeModal = mensaje;
    this.tipoModal = tipo;
    this.mostrarModal = true;
  }
}
