import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ISalaryGeneratorMaster } from '../../../shared/legacy/legacy-model/salary-generator-master.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { createRequestOption } from '../../../core/request/request-util';
import { IApprovalDTO } from '../../../shared/model/approval-dto.model';

type EntityResponseType = HttpResponse<ISalaryGeneratorMaster>;
type EntityArrayResponseType = HttpResponse<ISalaryGeneratorMaster[]>;
type BooleanResponseType = HttpResponse<boolean>;

@Injectable({ providedIn: 'root' })
export class SalaryLockService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/salary-lock');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISalaryGeneratorMaster[]>(this.resourceUrl + '/salaries', { params: options, observe: 'response' });
  }

  lockSelected(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrl + '/lock-selected', approvalDTO, { observe: 'response' });
  }

  unlockSelected(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.put<boolean>(this.resourceUrl + '/unlock-selected', approvalDTO, { observe: 'response' });
  }

  isSalaryLocked(month: string, year: string): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(this.resourceUrl + `/is-salary-locked/${month}/${year}`, { observe: 'response' });
  }
}
