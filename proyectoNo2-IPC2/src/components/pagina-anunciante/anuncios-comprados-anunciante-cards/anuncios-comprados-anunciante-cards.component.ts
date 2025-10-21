import { Component, Input } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { TipoAnuncioEnum } from '../../../models/anuncios/tipo-anuncios-enum';
import { Anuncio } from '../../../models/anuncios/anuncio';
import { DatePipe, NgClass, NgIf } from '@angular/common';

@Component({
  selector: 'app-anuncios-comprados-anunciante-cards',
  imports: [NgClass, DatePipe, NgIf],
  templateUrl: './anuncios-comprados-anunciante-cards.component.html',
  styleUrl: './anuncios-comprados-anunciante-cards.component.scss'
})
export class AnunciosCompradosAnuncianteCardsComponent {

  @Input() anuncioComprado!: Anuncio;

 constructor(private sanitizer: DomSanitizer) { }


    //Metodo que sirve para colocar los videos si end dado caso existen y verificar que la url sea segura
  getVideoUrl(): SafeResourceUrl {
    if (!this.anuncioComprado.url) return '';
    const autoplay = false; 
    const safeUrl = this.anuncioComprado.url.replace('autoplay=1', `autoplay=${autoplay ? 1 : 0}`);
    return this.sanitizer.bypassSecurityTrustResourceUrl(safeUrl);
  }

  //Metodo que ayuda a saber el tipo de anuncio que es 
  getTipoAnuncio(codigo: number): string {
    switch (codigo) {

      case 1: return 'ANUNCIO DE TEXTO';
      case 2: return 'ANUNCIO DE TEXTO E IMAGEN';
      case 3: return 'ANUNCIO DE VIDEO Y TEXTO';
      default: return 'DESCONOCIDO';
    }


  }

  //Metodo que sirve para activar o desactivar el anuncio en la web
  cambiarEstado(): void{
    this.anuncioComprado.estado = !this.anuncioComprado.estado; 
    
  }


}
