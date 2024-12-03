import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { INotification } from '../../shared/model/notification.model';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../core/config/application-config.service';

type EntityResponseType = HttpResponse<INotification>;

@Injectable({ providedIn: 'root' })
export class NotificationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('/api');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  getApprovalNotificationsCommon(): Observable<EntityResponseType> {
    return this.http.get<INotification>(this.resourceUrl + '/common/approval-notification', { observe: 'response' });
  }

  getApprovalNotificationsHR(): Observable<EntityResponseType> {
    return this.http.get<INotification>(this.resourceUrl + '/employee-mgt/approval-notification', { observe: 'response' });
  }
}
