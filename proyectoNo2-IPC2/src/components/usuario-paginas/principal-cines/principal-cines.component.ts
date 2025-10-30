import { Component, OnInit } from '@angular/core';
import { CinesCardComponent } from "../../cines/cines-card.component/cines-card.component";
import { Cine } from '../../../models/cines/cine';

@Component({
  selector: 'app-principal-cines.component',
  imports: [CinesCardComponent],
  templateUrl: './principal-cines.component.html',
  styleUrl: './principal-cines.component.scss'
})
export class PrincipalCinesComponent implements OnInit {




  //Pendiente jugar con las peticiones hacia el back
  showAd1 = true;
  showAd2 = false;
  showAd3 = false;
  showAd4 = true;
  showAd5 = false;
  showAd6 = true;



  cines: Cine[] = [];
  cinesMostrados: Cine[] = [];
  indiceActual = 0;
  cantidadPorCarga = 2;
  todosCargados = false;


  ngOnInit(): void {
    this.cines = [
      {
        codigo: 'CIN001',
        nombre: 'Cinemark Oakland',
        estadoAnuncio: true,
        montoOcultacion: 1500,
        fechaCreacion: '2024-02-01T00:00:00',
        descripcion: 'Uno de los cines más modernos de la ciudad con sonido Dolby Atmos.',
        ubicacion: 'Zona 10, Ciudad de Guatemala'
      },
      {
        codigo: 'CIN002',
        nombre: 'Cine Capitol',
        estadoAnuncio: false,
        montoOcultacion: 800,
        fechaCreacion: '2023-07-15T00:00:00',
        descripcion: 'Histórico cine restaurado con encanto clásico y proyecciones independientes.',
        ubicacion: 'Centro Histórico, Ciudad de Guatemala'
      },
      {
        codigo: 'CIN003',
        nombre: 'Cine Pradera Xela',
        estadoAnuncio: true,
        montoOcultacion: 1200,
        fechaCreacion: '2022-12-10T00:00:00',
        descripcion: 'Sala premium con butacas reclinables y pantalla gigante.',
        ubicacion: 'Quetzaltenango, Guatemala'
      },
      {
        codigo: 'CIN004',
        nombre: 'Cine Portales',
        estadoAnuncio: true,
        montoOcultacion: 1300,
        fechaCreacion: '2022-10-01T00:00:00',
        descripcion: 'Un cine con amplia oferta gastronómica y salas confortables.',
        ubicacion: 'Zona 17, Ciudad de Guatemala'
      }
    ];

    this.cargarMasCines(); // carga inicial
  }

  //Metodo que sirve para ir cargando mas cines dinamicamente
  cargarMasCines(): void {
    const siguienteBloque = this.cines.slice(
      this.indiceActual,
      this.indiceActual + this.cantidadPorCarga
    );

    this.cinesMostrados.push(...siguienteBloque);
    this.indiceActual += this.cantidadPorCarga;

    if (this.indiceActual >= this.cines.length) {
      this.todosCargados = true;
    }
  }



}
