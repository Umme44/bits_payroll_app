import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IApprovalDTO } from '../../../shared/model/approval-dto.model';
import { IRecruitmentRequisitionForm } from '../../../shared/legacy/legacy-model/recruitment-requisition-form.model';
import { createRequestOption } from '../../../core/request/request-util';
type BooleanResponseType = HttpResponse<boolean>;
type EntityArrayResponseType = HttpResponse<IRecruitmentRequisitionForm[]>;

@Injectable({ providedIn: 'root' })
export class RRFApprovalService {
  public resourceUrl = SERVER_API_URL + 'api/common/rrf/approval';

  constructor(protected http: HttpClient) {}

  getAllPending(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRecruitmentRequisitionForm[]>(this.resourceUrl + '/pending-list', { params: options, observe: 'response' });
  }

  approveSelected(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrl + '/approve-selected', approvalDTO, { observe: 'response' });
  }

  denySelected(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrl + '/reject-selected', approvalDTO, { observe: 'response' });
  }
}
