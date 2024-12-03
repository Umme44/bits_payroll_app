import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { INavBarItemAccessControl } from 'app/shared/model/nav-bar-item-access-control.model';
import { ApplicationConfigService } from '../../core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class NavbarService {
  private messageService = new BehaviorSubject('show');
  currentMessage = this.messageService.asObservable();
  public dashBoardAccessControlUrl = this.applicationConfigService.getEndpointFor('api/common/nav-bar-item-access-control');
  public employeeTotalWorkingDays = this.applicationConfigService.getEndpointFor('api/common/employees/total-working-days');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  changeMessage(message: string): void {
    this.messageService.next(message);
  }
  getAllAccessControl(): Observable<HttpResponse<INavBarItemAccessControl>> {
    return this.http.get<INavBarItemAccessControl>(this.dashBoardAccessControlUrl, { observe: 'response' });
  }

  getTotalWorkingDays(): Observable<HttpResponse<number>> {
    return this.http.get<number>(this.employeeTotalWorkingDays, { observe: 'response' });
  }
}
