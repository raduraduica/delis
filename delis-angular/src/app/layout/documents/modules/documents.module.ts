import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgSelectModule } from '@ng-select/ng-select';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BsDatepickerModule } from 'ngx-bootstrap';

import { DocumentsRoutingModule } from '../documents-routing.module';
import { DocumentsComponent } from '../components/documents.component';
import { PageHeaderModule } from '../../../shared/index';
import { DocumentsService } from '../services/documents.service';

@NgModule({
  imports: [CommonModule, FormsModule, NgbModule, NgSelectModule, DocumentsRoutingModule, PageHeaderModule, BsDatepickerModule],
  declarations: [DocumentsComponent],
  providers: [DocumentsService]
})
export class DocumentsModule {}