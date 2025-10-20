import { Component, OnInit } from '@angular/core';
import { SalaMasGustadaDTO } from '../../../models/reportes/sala-mas-gustada-dto';
import { SalasGustadasCardsComponent } from "../salas-gustadas-cards/salas-gustadas-cards.component";
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-reporte-salas-gustadas',
  imports: [SalasGustadasCardsComponent, NgClass, ReactiveFormsModule],
  templateUrl: './reporte-salas-gustadas.component.html',
  styleUrl: './reporte-salas-gustadas.component.scss'
})
export class ReporteSalasGustadasComponent implements OnInit {

  reporteSalaGustada: SalaMasGustadaDTO[] = [];
  reportesMostrados: SalaMasGustadaDTO[] = [];

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

    this.reporteSalaGustada = [
      {
        codigo: "S001",
        nombre: "Sala Premium 3D",
        filas: 12,
        columnas: 20,
        ubicacion: "Primer Nivel Interplaza",
        totalVentasBoleto: 450,
        usuarios: [
          {
            id: "juan-U001",
            identificacion: "pilininsanotr2",
            nombre: "Carlos López",
            correo: "pablomaldonado@mail.com"
          },
          {
            id: "U002",
            identificacion: "123456789",
            nombre: "Ana Pérez",
            correo: "ana.perez@mail.com"
          },
          {
            id: "U003",
            identificacion: "654987321",
            nombre: "Luis Martínez",
            correo: "luis.martinez@mail.com"
          }
        ]
      },
      {
        codigo: "S002",
        nombre: "Sala Adultos 2D",
        filas: 13,
        columnas: 15,
        ubicacion: "Segundo nivel walmart",
        totalVentasBoleto: 350,
        usuarios: [
          {
            id: "U001",
            identificacion: "987654321",
            nombre: "Carlos López",
            correo: "carlos.lopez@mail.com"
          },
          {
            id: "U002",
            identificacion: "123456789",
            nombre: "Ana Pérez",
            correo: "ana.perez@mail.com"
          },
          {
            id: "U003",
            identificacion: "654987321",
            nombre: "Luis Martínez",
            correo: "luis.martinez@mail.com"
          }
        ]
      }
    ];



    this.cargarMasReportes();
  }


  //Apartado de metodos para gestionar los filtros
  cargarMasReportes(): void {
    const siguienteBloque = this.reporteSalaGustada.slice(
      this.indiceActual,
      this.indiceActual + this.cantidadPorCarga
    );

    this.reportesMostrados.push(...siguienteBloque);
    this.indiceActual += this.cantidadPorCarga;

    if (this.indiceActual >= this.reporteSalaGustada.length) {
      this.todosCargados = true;
    }
  }

  //Metodo que sirve para exportar reportes
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
