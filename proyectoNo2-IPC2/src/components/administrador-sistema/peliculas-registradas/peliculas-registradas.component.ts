import { Component, OnInit } from '@angular/core';
import { CommonModule, NgIf } from '@angular/common';
import { PeliculasCardsRegistradasComponent } from "../peliculas-cards-registradas/peliculas-cards-registradas.component";
import { Pelicula } from '../../../models/peliculas/pelicula';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { PeliculaSistemaService } from '../../../services/peliculas-service/peliculas-sistema.service';

@Component({
  selector: 'app-peliculas-registradas',
  imports: [NgIf, CommonModule, PeliculasCardsRegistradasComponent, RouterLink, RouterLinkActive],
  templateUrl: './peliculas-registradas.component.html',
  styleUrl: './peliculas-registradas.component.scss'
})
export class PeliculasRegistradasComponent implements OnInit {



  peliculasMostradas: Pelicula[] = [];

  indiceActual = 0;
  cantidadPorCarga = 2;
  totalReportes = 0;
  todosCargados = false;

  constructor(
    private peliculasService: PeliculaSistemaService,
  ) {

  }


  //Metodo encargado de cargar el service
  ngOnInit(): void {

    this.cargarMasPeliculas();

  }

  //Metodo que permite cargar mas peliculas
  cargarMasPeliculas(): void {

    if (this.todosCargados) return;

    this.peliculasService.listadoRegistros().subscribe({
      next: (response: Pelicula[]) => {
        this.peliculasMostradas = response;

      },
      error: (error: any) => {


      }
    });


  }

}
