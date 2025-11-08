import { CommonModule, NgClass, NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TipoAnuncioEnum } from '../../../models/anuncios/tipo-anuncios-enum';
import { Popup } from '../../../shared/popup/popup';
import { SharedPopupComponent } from "../../pop-ups/shared-popup.component/shared-popup.component";
import { VigenciaAnuncioEnum } from '../../../models/anuncios/vigencia-anuncio-enum';
import { ConfiguracionAnuncioDTO } from '../../../models/anuncios/configuracion-anuncio-dto';
import { ConfiguracionAnunciosService } from '../../../services/anuncios-service/configuracion-anuncios.service';
import { ConfiguracionAnunciosCardsComponent } from "../configuracion-anuncios-cards/configuracion-anuncios-cards.component";
import { forkJoin } from 'rxjs';
import { VigenciaAnuncio } from '../../../models/anuncios/vigencia-anuncio';
import { VigenciaAnunciosCardsComponent } from "../vigencia-anuncios-cards/vigencia-anuncios-cards.component";
import { VigenciaAnunciosService } from '../../../services/anuncios-service/vigencia-anuncios.service';

@Component({
  selector: 'app-comprar-anuncio',
  imports: [ReactiveFormsModule, CommonModule, NgFor, SharedPopupComponent, NgIf, ConfiguracionAnunciosCardsComponent, VigenciaAnunciosCardsComponent],
  templateUrl: './comprar-anuncio.component.html',
  styleUrl: './comprar-anuncio.component.scss',
  providers: [Popup],
})
export class ComprarAnuncioComponent implements OnInit {


  anuncioForm!: FormGroup;
  pagoForm!: FormGroup;

  configuracionesAnuncios: ConfiguracionAnuncioDTO[] = [];

  vigenciasAnuncios: VigenciaAnuncio[] = [];

  tipoSeleccionado: number | null = null;
  tiposAnuncio: { codigo: number, label: string }[] = [];

  tiposTarifa: { codigo: number, label: string }[] = [];

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
    private formBuilder: FormBuilder,
    private configuracionAnunciosService: ConfiguracionAnunciosService,
    private vigenciaAnunciosService: VigenciaAnunciosService) { }

  ngOnInit(): void {

    this.cargarCostosAnuncios();

    // Cargar los tipos de anuncio desde el enum
    this.tiposAnuncio = [
      { codigo: 1, label: TipoAnuncioEnum.ANUNCIO_TEXTO },
      { codigo: 2, label: TipoAnuncioEnum.IMAGEN_TEXTO },
      { codigo: 3, label: TipoAnuncioEnum.VIDEO_TEXTO },
    ];

    // Cargar los tipos de anuncio desde el enum
    this.tiposTarifa = [
      { codigo: 1, label: VigenciaAnuncioEnum.UN_DIA },
      { codigo: 2, label: VigenciaAnuncioEnum.TRES_DIAS },
      { codigo: 3, label: VigenciaAnuncioEnum.UNA_SEMANA },
      { codigo: 4, label: VigenciaAnuncioEnum.DOS_SEMANAS },
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
      codigoTarifa: [1, Validators.required],
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

    this.anuncioForm.valueChanges.subscribe(() => {
      if (this.mostrarSegundoFormulario) {
        this.mostrarSegundoFormulario = false;
        this.pagoForm.reset({ metodo: '', monto: 0 });
      }
    });

    // Detectar cambio en tipo seleccionado
    this.anuncioForm.get('codigoTipo')?.valueChanges.subscribe(valor => {
      this.tipoSeleccionado = valor;
    });
  }


  //Metodo delegado para cargar los recursos para mostrar los costos de compra de anuncios
  cargarCostosAnuncios(): void {

    this.configuracionAnunciosService.listadoConfiguraciones().subscribe({
      next: (response: ConfiguracionAnuncioDTO[]) => {


        this.configuracionesAnuncios = response;

      },
      error: (error: any) => {

        this.mostrarError(error);

      }
    });

    this.vigenciaAnunciosService.listadoVigencias().subscribe({
      next: (response: VigenciaAnuncio[]) => {


        this.vigenciasAnuncios = response;

      },
      error: (error: any) => {

        this.mostrarError(error);

      }
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
    this.formDataAnuncio.append('tipoTarifa', this.anuncioForm.value.codigoTarifa);

    if (this.imagenFile) {
      this.formDataAnuncio.append('foto', this.imagenFile);
    }

    this.calcularTotalListo(this.anuncioForm.value.codigoTipo, 0);


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
    this.mostrarSegundoFormulario = false;
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



    this.formDataAnuncio.append('metodoPago', this.pagoForm.value.metodo);
    this.formDataAnuncio.append('monto', this.pagoForm.getRawValue().monto);

    console.log('FormData completo listo para enviar:', this.formDataAnuncio);

    // Ahora sí mandas TODO con tu servicio
    // this.miServicio.enviarTodo(this.formDataAnuncio).subscribe(...)
  }

  precioConfig!: number;
  precioOtro!: number;

  //Metodo que permite calcular el total
  calcularTotalListo(idConfig: number, idTarifa: number): void {

    forkJoin({
      config: this.configuracionAnunciosService.configuracionAnuncioCodigo(idConfig),
      //tarifa: this.tarifaService.obtenerTarifa(idTarifa)
    })
      .subscribe({
        next: ({ config }) => {

          const total = config.precio;

          this.pagoForm.patchValue({
            monto: total
          });
        },
        error: (e) => this.mostrarError(e)
      });


  }

  //Metodo que permite calcular el precio por configuracion
  obtenerPrecioConfig(idConfig: number): void {
    this.configuracionAnunciosService.configuracionAnuncioCodigo(idConfig)
      .subscribe({
        next: (config: ConfiguracionAnuncioDTO) => {
          console.log(config.precio);
          this.precioConfig = config.precio;
        }
      });
  }

  obtenerPrecioTarifa(idTarifa: string): void {
    /* this.otroService.obtenerAlgo(id)
       .subscribe({
         next: (otro) => {
           this.precioOtro = otro.precioBase;
           this.calcularTotalSiListo(this.pagoForm.value.dias);
         }
       });*/
  }


}
