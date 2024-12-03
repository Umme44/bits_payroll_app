import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import Swal from 'sweetalert2';
import { INomineeValidation } from '../../model/nominee-validation.model';
import { INominee } from '../legacy-model/nominee.model';
import { IPfNominee } from '../legacy-model/pf-nominee.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';

type ResponseType = HttpResponse<INomineeValidation>;

@Injectable({ providedIn: 'root' })
export class NomineeValidationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/common');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  queryForPreValidateNominee(nominee: INominee): Observable<ResponseType> {
    return this.http.post<INomineeValidation>(this.resourceUrl + '/validate-nominee', nominee, { observe: 'response' });
  }

  queryForPreValidateNomineeForAdmin(nominee: INominee): Observable<ResponseType> {
    return this.http.post<INomineeValidation>(this.resourceUrl + '/validate-nominee-admin', nominee, { observe: 'response' });
  }

  queryForPreValidatePFNominee(nominee: IPfNominee): Observable<ResponseType> {
    return this.http.post<INomineeValidation>(this.resourceUrl + '/validate-pf-nominee', nominee, { observe: 'response' });
  }

  swalValidationMsg(msg: string): void {
    Swal.fire({
      icon: 'warning',
      text: msg,
      timer: 3000,
      showConfirmButton: false,
    });
  }
}
