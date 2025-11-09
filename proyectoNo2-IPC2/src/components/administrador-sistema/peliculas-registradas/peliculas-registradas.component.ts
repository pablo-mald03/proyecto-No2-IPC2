import { Component, OnInit } from '@angular/core';
import { CommonModule, NgIf } from '@angular/common';
import { PeliculasCardsRegistradasComponent } from "../peliculas-cards-registradas/peliculas-cards-registradas.component";
import { Pelicula } from '../../../models/peliculas/pelicula';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-peliculas-registradas',
  imports: [NgIf, CommonModule, PeliculasCardsRegistradasComponent,RouterLink, RouterLinkActive],
  templateUrl: './peliculas-registradas.component.html',
  styleUrl: './peliculas-registradas.component.scss'
})
export class PeliculasRegistradasComponent implements OnInit {



  peliculasMostradas: Pelicula[] = [];

  indiceActual = 0;
  cantidadPorCarga = 2;
  totalReportes = 0;
  todosCargados = false;


  //Metodo encargado de cargar el service
  ngOnInit(): void {
   


  }

  //Metodo que permite cargar mas peliculas
  cargarMasPeliculas(): void {

    if (this.todosCargados) return;
    /*
        this.cineService.listadoRegistros(this.cantidadPorCarga, this.indiceActual).subscribe({
          next: (response: CineDTO[]) => {
            this.ampliarResultados(response);
    
          },
          error: (error: any) => {
    
            this.mostrarError(error);
    
          }
        });
    */

  }

}
