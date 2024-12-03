import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { IDashboardItemAccessControl } from '../shared/model/dashboard-item-access-control.model';
import { ApplicationConfigService } from '../core/config/application-config.service';
import { createRequestOption } from '../core/request/request-util';
import { IOfficeNotices } from '../shared/legacy/legacy-model/office-notices.model';

type EntityResponseType = HttpResponse<IOfficeNotices>;
type EntityArrayResponseType = HttpResponse<IOfficeNotices[]>;

@Injectable({ providedIn: 'root' })
export class DashboardService {
  public noticeUrl = this.applicationConfigService.getEndpointFor('api/common/office-notices');
  public recentNoticeUrl = this.applicationConfigService.getEndpointFor('api/common/recent-office-notices');
  public anySubOrdinateUrl = this.applicationConfigService.getEndpointFor('api/common/has-subordinate');
  public isEmployeeEligibleForInsurance = this.applicationConfigService.getEndpointFor(
    'api/common/insurance-registrations/is-employee-eligible'
  );
  public isEmployeeEligibleForGF = this.applicationConfigService.getEndpointFor('api/common/nominees/is-eligible-for-gf');
  public isEmployeeEligibleForGeneral = this.applicationConfigService.getEndpointFor('api/common/nominees/is-eligible-for-general-nominee');
  public isEmployeeEligibleForPF = this.applicationConfigService.getEndpointFor('api/common/pf-nominees-form/is-employee-eligible');
  public dashBoardAccessControlUrl = this.applicationConfigService.getEndpointFor('api/common/dashboard-item-access-control');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  findAllNotices(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOfficeNotices[]>(this.noticeUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertNoticeDateFromServer(res)));
  }

  findAllRecentNotices(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IOfficeNotices[]>(this.recentNoticeUrl, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertNoticeDateFromServer(res)));
  }

  anySubOrdinate(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(this.anySubOrdinateUrl, { observe: 'response' });
  }

  isEmployeeEligibleForInsuranceRegistration(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(this.isEmployeeEligibleForInsurance, { observe: 'response' });
  }

  isEmployeeEligibleForGratuityFund(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(this.isEmployeeEligibleForGF, { observe: 'response' });
  }

  isEmployeeEligibleForGeneralNominee(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(this.isEmployeeEligibleForGeneral, { observe: 'response' });
  }

  isEmployeeEligibleForProvidentFund(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(this.isEmployeeEligibleForPF, { observe: 'response' });
  }

  getAllAccessControl(): Observable<HttpResponse<IDashboardItemAccessControl>> {
    return this.http.get<IDashboardItemAccessControl>(this.dashBoardAccessControlUrl, { observe: 'response' });
  }

  protected convertNoticeDateFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((officeNotices: IOfficeNotices) => {
        officeNotices.publishForm = officeNotices.publishForm ? dayjs(officeNotices.publishForm) : undefined;
        officeNotices.publishTo = officeNotices.publishTo ? dayjs(officeNotices.publishTo) : undefined;
        officeNotices.createdAt = officeNotices.createdAt ? dayjs(officeNotices.createdAt) : undefined;
        officeNotices.updatedAt = officeNotices.updatedAt ? dayjs(officeNotices.updatedAt) : undefined;
      });
    }
    return res;
  }
}
