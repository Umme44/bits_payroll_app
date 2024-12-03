import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';
import { Subscription } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { VERSION } from 'app/app.constants';
import { LANGUAGES } from 'app/config/language.constants';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { EntityNavbarItems } from 'app/entities/entity-navbar-items';
import { IEmployee } from '../../entities/employee/employee.model';
import { INotification } from '../../shared/model/notification.model';
import { NavbarService } from './navbar.service';
import { NotificationService } from './notification.service';
import { Authority } from '../../config/authority.constants';
import { UserFeedbackService } from '../../entities/user-feedback/service/user-feedback.service';
import { EmployeeCommonService } from '../../shared/service/employee-common.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SpecialAccessModalComponent } from '../../dashboard/special-access-modal/special-access-modal.component';
import { OrganizationFilesUrl } from '../../shared/constants/organization-files-url';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar-prod.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;
  entitiesNavbarItems: any[] = [];

  imgError = 'Showing error';
  imgSrc: string | ArrayBuffer = '../../content/images/profile-placeholder.png';
  welcomeWish!: string;

  employees: IEmployee[] = [];
  selectedEmployee!: number;
  loadSearchBar = false;

  hrNotifications!: INotification;
  hideNavbar = false;

  navbarStatus!: string;
  subscription!: Subscription;
  canManageTaxAcknowledgementReceipt = false;

  totalWorkingDays = 0;

  constructor(
    private navbarService: NavbarService,
    private notificationService: NotificationService,
    private sessionStorage: SessionStorageService,
    private userFeedbackService: UserFeedbackService,
    protected employeeService: EmployeeCommonService,
    // private searchModalService: SearchModalService,
    protected modalService: NgbModal,
    private loginService: LoginService,
    private translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private router: Router
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.entitiesNavbarItems = EntityNavbarItems;
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });

    this.loadSearchBar = false;
    this.hideNavbar = false;

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      if (account == null) this.router.navigate(['/']);
      else {
        if (this.accountService.hasAnyAuthority([Authority.ADMIN, Authority.HR_ADMIN, Authority.EMPLOYEE, Authority.USER])) {
          if (location.pathname === '/') {
            this.router.navigate(['dashboard']);
          }
          this.accountService.identity().subscribe(account => {
            if (account) {
              this.account = account;
              this.loadImage();
              if (this.isAuthenticated()) {
                this.getEmployeesTotalWorkingDay();
              }
            }
          });
        }
      }
    });

    const currentHour = new Date().getHours();
    if (currentHour <= 12) this.welcomeWish = 'Good Morning';
    else if (currentHour <= 15) this.welcomeWish = 'Good Noon';
    else if (currentHour <= 17) this.welcomeWish = 'Good Afternoon';
    else this.welcomeWish = 'Good Evening';
    this.subscription = this.navbarService.currentMessage.subscribe(statusMessage => (this.navbarStatus = statusMessage));
  }

  changeLanguage(languageKey: string): void {
    this.sessionStorageService.store('locale', languageKey);
    this.translateService.use(languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    //load user feedback page before logout
    this.userFeedbackService.isAllowed().subscribe(res => {
      if (res.body === false) {
        this.collapseNavbar();
        this.loginService.logout();
        this.router.navigate(['']);
      } else {
        this.hideNavbar = true;
        this.router.navigate(['/user-feedback/rating']);
      }
    });
    sessionStorage.clear();
    this.canManageTaxAcknowledgementReceipt = false;
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  checkTaxManagement(): void {
    if (this.isAuthenticated()) {
      const value = sessionStorage.getItem('navBarItemAccessControl');
      if (value !== null && value !== undefined) {
        const navBarItemAccessControl = JSON.parse(value);
        this.canManageTaxAcknowledgementReceipt = navBarItemAccessControl.canManageTaxAcknowledgementReceipt!;
      } else {
        this.navbarService.getAllAccessControl().subscribe(res => {
          const navBarItemAccessControl = res.body!;
          sessionStorage.setItem('navBarItemAccessControl', JSON.stringify(navBarItemAccessControl));
          this.canManageTaxAcknowledgementReceipt = navBarItemAccessControl.canManageTaxAcknowledgementReceipt!;
        });
      }
    }
  }

  loadNotifications(): void {
    if (this.isAuthenticated() && this.accountService.hasAnyAuthority([Authority.ADMIN, Authority.HR_ADMIN])) {
      this.notificationService.getApprovalNotificationsHR().subscribe((res: HttpResponse<INotification>) => {
        this.hrNotifications = res.body!;
      });
    }
  }

  getEmployeesTotalWorkingDay(): void {
    this.navbarService.getTotalWorkingDays().subscribe((res: HttpResponse<number>) => {
      if (res.body! >= 0) {
        this.totalWorkingDays = res.body!;
      }
    });
  }

  getImageUrl(): string {
    return this.isAuthenticated() ? this.accountService.getImageUrl() : '';
  }

  private createImage(image: Blob): void {
    if (image && image.size > 0) {
      const reader = new FileReader();

      reader.addEventListener(
        'load',
        () => {
          this.imgSrc = reader.result!;
        },
        false
      );
      reader.readAsDataURL(image);
    }
  }

  loadImage(): void {
    this.employeeService.getCurrentDP().subscribe(
      data => {
        this.createImage(data);
      },
      error => {
        this.imgError = error;
      }
    );
  }

  openSpecialAccessModal(): void {
    const modalRef = this.modalService.open(SpecialAccessModalComponent, { backdrop: 'static', centered: true });
  }

  showUIFriendlyName(firstName: any, lastName: any): string {
    if (firstName === null || firstName === undefined) {
      firstName = '';
    }

    if (lastName === null || lastName === undefined) {
      lastName = '';
    }

    if (firstName.toString().concat(lastName.toString()).length > 15) {
      if (firstName.toString().length <= lastName.toString().length) {
        return firstName;
      }
      return lastName;
    }
    return firstName + ' ' + lastName;
  }

  getLogoUrl(): string {
    return OrganizationFilesUrl.LOGO;
  }
}
