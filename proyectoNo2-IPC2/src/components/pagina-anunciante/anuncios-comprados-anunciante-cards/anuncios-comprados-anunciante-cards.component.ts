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

      case 1: return TipoAnuncioEnum.ANUNCIO_TEXTO;
      case 2: return TipoAnuncioEnum.IMAGEN_TEXTO;
      case 3: return TipoAnuncioEnum.VIDEO_TEXTO;
      default: return 'Desconocido';
    }


  }

  //Metodo que sirve para activar o desactivar el anuncio en la web
  cambiarEstado(): void{

    
  }


}
