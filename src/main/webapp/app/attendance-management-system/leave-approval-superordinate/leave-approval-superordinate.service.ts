import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IApprovalDTO } from '../../shared/model/approval-dto.model';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { createRequestOption } from '../../core/request/request-util';
import { ILeaveAllocation } from '../../shared/legacy/legacy-model/leave-allocation.model';
import { ILeaveBalanceEndUserView } from '../../shared/legacy/legacy-model/leave-balance-end-user-view.model';
import { ILeaveApplication, LeaveApplication } from '../../shared/legacy/legacy-model/leave-application.model';

type BooleanResponseType = HttpResponse<boolean>;
type EntityArrayResponseType = HttpResponse<ILeaveAllocation[]>;
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { IMovementEntry } from '../../shared/legacy/legacy-model/movement-entry.model';
@Injectable({ providedIn: 'root' })
export class LeaveApprovalLmService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('/api/common/leave-application');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getAllPending(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaveBalanceEndUserView[]>(this.resourceUrl + '/lm', { params: options, observe: 'response' })
      .pipe(map(res => this.convertDateArrayFromServer(res)));
  }

  approveAll(): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrl + '/approve-all-lm', new FormData(), { observe: 'response' });
  }

  denyAll(): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrl + '/reject-all-lm', new FormData(), { observe: 'response' });
  }

  approveSelected(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrl + '/approve-selected-lm', approvalDTO, { observe: 'response' });
  }

  denySelected(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrl + '/reject-selected-lm', approvalDTO, { observe: 'response' });
  }

  getAllApprovedByMe(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<[LeaveApplication]>(this.resourceUrl + '/approved-by-me', {
      params: options,
      observe: 'response',
    });
  }

  getAllApprovedByMeFilter(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<[LeaveApplication]>(this.resourceUrl + '/approved-by-me-filter', {
      params: options,
      observe: 'response',
    });
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((leaveApplication: ILeaveApplication) => {
        leaveApplication.startDate = leaveApplication.startDate ? dayjs(leaveApplication.startDate) : undefined;
        leaveApplication.endDate = leaveApplication.endDate ? dayjs(leaveApplication.endDate) : undefined;
      });
    }
    return res;
  }

  approveById(id: any): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrl + `/approve-lm/${id}`, null, { observe: 'response' });
  }

  rejectById(id: any): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrl + `/reject-lm/${id}`, null, { observe: 'response' });
  }
}
