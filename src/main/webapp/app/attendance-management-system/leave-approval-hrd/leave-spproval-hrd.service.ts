import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IApprovalDTO } from 'app/shared/model/approval-dto.model';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { ILeaveApplication } from '../../shared/legacy/legacy-model/leave-application.model';
import { createRequestOption } from '../../core/request/request-util';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { IEmploymentHistory } from '../../shared/legacy/legacy-model/employment-history.model';

type BooleanResponseType = HttpResponse<boolean>;
type EntityArrayResponseType = HttpResponse<ILeaveApplication[]>;

@Injectable({ providedIn: 'root' })
export class LeaveApprovalHrdService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('/api/attendance-mgt/leave-application');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getAllPending(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaveApplication[]>(this.resourceUrl + '/hr', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  approveAll(): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrl + '/approve-all-hr', null, { observe: 'response' });
  }

  approveById(id: any): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrl + `/approve-hr/${id}`, null, { observe: 'response' });
  }

  rejectById(id: any): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrl + `/reject-hr/${id}`, null, { observe: 'response' });
  }

  denyAll(): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrl + '/reject-all-hr', null, { observe: 'response' });
  }

  approveSelected(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrl + '/approve-selected-hr', approvalDTO, { observe: 'response' });
  }

  denySelected(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrl + '/reject-selected-hr', approvalDTO, { observe: 'response' });
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((leaveApplication: ILeaveApplication) => {
        leaveApplication.applicationDate = leaveApplication.applicationDate ? dayjs(leaveApplication.applicationDate) : undefined;
        leaveApplication.startDate = leaveApplication.startDate ? dayjs(leaveApplication.startDate) : undefined;
        leaveApplication.endDate = leaveApplication.endDate ? dayjs(leaveApplication.endDate) : undefined;
      });
    }
    return res;
  }
}
