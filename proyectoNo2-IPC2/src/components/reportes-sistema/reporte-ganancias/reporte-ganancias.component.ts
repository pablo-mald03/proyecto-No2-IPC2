import { Component } from '@angular/core';
import { GananciasSistemaDTO } from '../../../models/reportes/ganancias-sistema-dto';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgClass } from '@angular/common';
import { ReporteGanaciasCardsComponent } from "../reporte-ganacias-cards.component/reporte-ganacias-cards.component";

@Component({
  selector: 'app-reporte-ganancias',
  imports: [ReactiveFormsModule, NgClass, ReporteGanaciasCardsComponent],
  templateUrl: './reporte-ganancias.component.html',
  styleUrl: './reporte-ganancias.component.scss'
})
export class ReporteGananciasComponent {

  //Apartado de atributos que sirven para exponer los dto
  reporteGanancias: GananciasSistemaDTO[] = [];
  reportesMostrados: GananciasSistemaDTO[] = [];

  //Apartado de atributos que sirven para cargar dinamicamente los atributos
  indiceActual = 0;
  cantidadPorCarga = 2;
  todosCargados = false;

  //Atributos que sirven para gestionar los filtros
  filtrosForm!: FormGroup;

  constructor(
    private formBuild: FormBuilder

  ) { }


  ngOnInit(): void {

    //Se teneran los filtros reactivos
    this.filtrosForm = this.formBuild.group({
      fechaInicio: [null],
      fechaFin: [null]
    });

    this.reporteGanancias = [
      {
        costosCine: [
          {
            codigo: 'CIN-001',
            nombre: 'Alba Cinema',
            montoOcultacion: 2000,
            fechaCreacion: new Date('2025-02-15'),
            costos: [
              {
                costo: 300,
                fechaModificacion: new Date('2025-03-15')

              },
              {
                costo: 200,
                fechaModificacion: new Date('2025-04-15')

              }
            ]
          },
          {
            codigo: 'CIN-002',
            nombre: 'Cinepolis',
            montoOcultacion: 1000,
            fechaCreacion: new Date('2025-01-15'),
            costos: [
              {
                costo: 400,
                fechaModificacion: new Date('2025-03-12')

              },
              {
                costo: 500,
                fechaModificacion: new Date('2025-04-13')

              }
            ]
          }
        ],

        anunciosComprados: [
          {
            codigo: 'AN-2025-001',
            nombre: 'Campaña de Verano - CineNova',
            fechaCompra: new Date('2025-03-12')
          },
          {
            codigo: 'AN-2025-002',
            nombre: 'Promoción Película Épica',
            fechaCompra: new Date('2025-06-01')
          },
          {
            codigo: 'AN-2025-003',
            nombre: 'Lanzamiento Serie Animada',
            fechaCompra: new Date('2025-08-25')
          }
        ],

        pagoBloqueoAnuncios: [
          {
            idUsuario: 'USR-001',
            monto: 450,
            fechaPago: new Date('2025-04-20')
          },
          {
            idUsuario: 'USR-002',
            monto: 600,
            fechaPago: new Date('2025-07-10')
          },
          {
            idUsuario: 'USR-003',
            monto: 300,
            fechaPago: new Date('2025-09-03')
          }
        ],

        totalCostoCine: 30000,
        totalIngresos: 68500,
        totalGanancia: 38500
      }
    ];


    this.cargarMasReportes();
  }


  //Metodo que sirve para cargar mas y no mostrar todos de golpe
  cargarMasReportes(): void {
    const siguienteBloque = this.reporteGanancias.slice(
      this.indiceActual,
      this.indiceActual + this.cantidadPorCarga
    );

    this.reportesMostrados.push(...siguienteBloque);
    this.indiceActual += this.cantidadPorCarga;

    if (this.indiceActual >= this.reporteGanancias.length) {
      this.todosCargados = true;
    }
  }


  //Metodo que sirve para mandar a exportar el reporte
  exportarReporte() {


  }

  //Metodo get que sirve para validar si la fecha inicial antecede a la final
  get fechaInvalida(): boolean {
    const inicio = this.filtrosForm.get('fechaInicio')?.value;
    const fin = this.filtrosForm.get('fechaFin')?.value;
    if (!inicio || !fin) return false;
    return new Date(inicio) > new Date(fin);
  }

  //Metodo que sirve para validar las fechas
  validarFechas() {
    const { fechaInicio, fechaFin } = this.filtrosForm.value;
    if (fechaInicio && fechaFin && new Date(fechaInicio) > new Date(fechaFin)) {
      this.filtrosForm.setErrors({ fechaInvalida: true });
    } else {
      this.filtrosForm.setErrors(null);
    }
  }

  //Metodo que sirve para filtrar
  filtrar(): void {

    if (this.filtrosForm.invalid || this.fechaInvalida) return;

    const { fechaInicio, fechaFin } = this.filtrosForm.value;

    const inicioISO = fechaInicio ? new Date(fechaInicio).toISOString() : null;
    const finISO = fechaFin ? new Date(fechaFin).toISOString() : null;

    //Pendiente hacer la query
    console.log('trilin');

  }

  limpiarFechas(): void {
    this.filtrosForm.reset();
  }




}
