import { Component } from '@angular/core';
import { AnuncioRegistradoDTO } from '../../../models/anuncios/anuncio-registrado-dto';
import { AnunciosRegistradosSistemaCardsComponent } from "../anuncios-registrados-sistema-cards/anuncios-registrados-sistema-cards.component";

@Component({
  selector: 'app-anuncios-registrados-sistema',
  imports: [AnunciosRegistradosSistemaCardsComponent],
  templateUrl: './anuncios-registrados-sistema.component.html',
  styleUrl: './anuncios-registrados-sistema.component.scss'
})
export class AnunciosRegistradosSistemaComponent {

  idUsuario!: string;

  //Arreglos que se van a llenar en base a los anuncios que posea el usuario
  anuncios: AnuncioRegistradoDTO[] = [];
  anunciosMostrados: AnuncioRegistradoDTO[] = [];

  indiceActual = 0;
  cantidadPorCarga = 2;
  todosCargados = false;


  constructor(
    //Pendiente instanciar al service

  ) {

  }

  //Metodo que permite tomar el parametro enviado
  ngOnInit(): void {



    



    //PENDIENTE LLAMAR AL SERVICE
    /* this.eventsService.getEventByCode(this.eventCode).subscribe({
      next: (eventToUpdate) => {
        this.eventToUpdate = eventToUpdate;
        this.exists = true;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
*/
    this.cargarMasAnuncios();

    console.log(this.idUsuario);

  }

  //Carga dinamicamente la cantidad establecida de anuncios para no saturar la web
  cargarMasAnuncios(): void {
    const siguienteBloque = this.anuncios.slice(
      this.indiceActual,
      this.indiceActual + this.cantidadPorCarga
    );

    this.anunciosMostrados.push(...siguienteBloque);
    this.indiceActual += this.cantidadPorCarga;

    if (this.indiceActual >= this.anuncios.length) {
      this.todosCargados = true;
    }
  }

}
