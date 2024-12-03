import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { createRequestOption } from '../../../core/request/request-util';
import { ISalaryGeneratorMaster } from '../legacy-model/salary-generator-master.model';
import { IApprovalDTO } from '../../model/approval-dto.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';

type EntityResponseType = HttpResponse<ISalaryGeneratorMaster>;
type EntityArrayResponseType = HttpResponse<ISalaryGeneratorMaster[]>;
type BooleanResponseType = HttpResponse<boolean>;

@Injectable({ providedIn: 'root' })
export class SalaryLockService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/salary-lock');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISalaryGeneratorMaster[]>(this.resourceUrl + '/salaries', {
      params: options,
      observe: 'response',
    });
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
