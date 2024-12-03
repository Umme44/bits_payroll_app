import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { createRequestOption } from 'app/core/request/request-util';
import { IRecruitmentRequisitionBudget, NewRecruitmentRequisitionBudget } from '../recruitment-requisition-budget.model';

type EntityResponseType = HttpResponse<IRecruitmentRequisitionBudget>;
type EntityArrayResponseType = HttpResponse<IRecruitmentRequisitionBudget[]>;

@Injectable({ providedIn: 'root' })
export class RecruitmentRequisitionBudgetService {
  public resourceUrl = SERVER_API_URL + 'api/employee-mgt/recruitment-requisition-budgets';
  public userResourceUrl = SERVER_API_URL + 'api/common/recruitment-requisition-budgets';

  constructor(protected http: HttpClient) {}

  create(recruitmentRequisitionBudget: IRecruitmentRequisitionBudget): Observable<EntityResponseType> {
    return this.http.post<IRecruitmentRequisitionBudget>(this.resourceUrl, recruitmentRequisitionBudget, { observe: 'response' });
  }

  update(recruitmentRequisitionBudget: IRecruitmentRequisitionBudget): Observable<EntityResponseType> {
    return this.http.put<IRecruitmentRequisitionBudget>(this.resourceUrl, recruitmentRequisitionBudget, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRecruitmentRequisitionBudget>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRecruitmentRequisitionBudget[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByLoggedInUserId(): Observable<EntityArrayResponseType> {
    return this.http.get<IRecruitmentRequisitionBudget[]>(`${this.userResourceUrl}`, { observe: 'response' });
  }
}
