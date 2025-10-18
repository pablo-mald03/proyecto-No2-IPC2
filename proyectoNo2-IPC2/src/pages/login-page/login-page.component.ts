import { Component } from '@angular/core';
import { RouterLinkActive, RouterModule } from '@angular/router';
import { FormLoginComponent } from "../../components/login-usuario/form-login.component/form-login.component";

@Component({
  selector: 'app-login-page.component',
  imports: [RouterModule, FormLoginComponent],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss'
})
export class LoginPageComponent {


}
