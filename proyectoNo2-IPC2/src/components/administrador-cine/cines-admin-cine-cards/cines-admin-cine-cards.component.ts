import { Component, Input } from '@angular/core';
import { CineDTO } from '../../../models/cines/cine-dto';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cines-admin-cine-cards',
  imports: [CommonModule],
  templateUrl: './cines-admin-cine-cards.component.html',
  styleUrl: './cines-admin-cine-cards.component.scss'
})
export class CinesAdminCineCardsComponent {

  @Input() cine!: CineDTO;



  //Metodo que permite ocultar los anuncios de un cine
  ocultarAnuncios(codigo:string){


  }

}
