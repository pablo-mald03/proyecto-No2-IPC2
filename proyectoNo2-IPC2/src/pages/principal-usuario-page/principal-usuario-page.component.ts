import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from "../../components/header/header.component";
import { FooterComponent } from "../../components/footer/footer.component";
import { RouterOutlet } from '@angular/router';
import { Cine } from '../../models/cines/cine';
import { CinesCardComponent } from '../../components/cines/cines-card.component/cines-card.component';

@Component({
  selector: 'app-principal-usuario-page',
  imports: [HeaderComponent, FooterComponent, CinesCardComponent],
  templateUrl: './principal-usuario-page.component.html',
  styleUrl: './principal-usuario-page.component.scss'
})
export class PrincipalUsuarioPageComponent implements OnInit {


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
