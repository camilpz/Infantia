import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { NavbarComponent } from "./components/navbar/navbar.component";
import { LandingNavbarComponent } from "./components/landing-navbar/landing-navbar.component";
import { LandingPageComponent } from "./pages/landing-page/landing-page.component";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, LoginComponent, NavbarComponent, LandingNavbarComponent, LandingPageComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
  isLoggedIn = false;
}
