import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApprovalDTO } from '../../../shared/model/approval-dto.model';
import { IFlexScheduleApplication } from '../../../shared/legacy/legacy-model/flex-schedule-application.model';
import { FlexScheduleApplicationService } from '../../../shared/legacy/legacy-service/flex-schedule-application.service';

type EntityArrayResponseType = HttpResponse<IFlexScheduleApplication[]>;
type BooleanResponseType = HttpResponse<boolean>;

@Injectable({ providedIn: 'root' })
export class FlexScheduleApplicationApprovalService extends FlexScheduleApplicationService {
  public resourceUrlHR = SERVER_API_URL + 'api/attendance-mgt/flex-schedule-applications-approval-hr';
  public resourceUrlLM = SERVER_API_URL + 'api/common/flex-schedule-applications-approval-lm';

  constructor(protected http: HttpClient) {
    super(http);
  }

  findAllPendingHR(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IFlexScheduleApplication[]>(this.resourceUrlHR + '/pending', { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  approveSelectedHR(listOfIds: number[]): Observable<BooleanResponseType> {
    const approvalDTO = { ...new ApprovalDTO(), listOfIds };
    return this.http.put<boolean>(`${this.resourceUrlHR}/approve-selected`, approvalDTO, { observe: 'response' });
  }

  denySelectedHR(listOfIds: number[]): Observable<BooleanResponseType> {
    const approvalDTO = { ...new ApprovalDTO(), listOfIds };
    return this.http.put<boolean>(`${this.resourceUrlHR}/deny-selected`, approvalDTO, { observe: 'response' });
  }

  findAllPendingLM(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IFlexScheduleApplication[]>(this.resourceUrlLM + '/pending', { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findAllApprovedByLM(requesterId?: number): Observable<EntityArrayResponseType> {
    if (requesterId) {
      return this.http
        .get<IFlexScheduleApplication[]>(this.resourceUrlLM + '/approved-by-me?requesterId=' + requesterId, { observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    } else {
      return this.http
        .get<IFlexScheduleApplication[]>(this.resourceUrlLM + '/approved-by-me', { observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }
  }

  findEmployeeListOfFlexScheduleApprovedByMe(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IFlexScheduleApplication[]>(this.resourceUrlLM + '/approved-employee-list', { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  approveSelectedLM(listOfIds: number[]): Observable<BooleanResponseType> {
    const approvalDTO = { ...new ApprovalDTO(), listOfIds };
    return this.http.put<boolean>(`${this.resourceUrlLM}/approve-selected`, approvalDTO, { observe: 'response' });
  }

  denySelectedLM(listOfIds: number[]): Observable<BooleanResponseType> {
    const approvalDTO = { ...new ApprovalDTO(), listOfIds };
    return this.http.put<boolean>(`${this.resourceUrlLM}/deny-selected`, approvalDTO, { observe: 'response' });
  }
}
