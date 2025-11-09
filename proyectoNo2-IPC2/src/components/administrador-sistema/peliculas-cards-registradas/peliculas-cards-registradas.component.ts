import { Component, Input } from '@angular/core';
import { Pelicula } from '../../../models/peliculas/pelicula';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-peliculas-cards-registradas',
  imports: [RouterLink, CommonModule],
  templateUrl: './peliculas-cards-registradas.component.html',
  styleUrl: './peliculas-cards-registradas.component.scss'
})
export class PeliculasCardsRegistradasComponent {


   @Input() pelicula!: Pelicula;

  get posterSrc(): string {
    return this.pelicula.poster
      ? `data:image/jpeg;base64,${this.pelicula.poster}`
      : 'icons-app/defaultImg.png'; 
  }
}
